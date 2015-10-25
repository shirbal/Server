package com.taru.pinki;

import java.util.*;

import com.taru.model.Transaction;
import com.taru.model.TransactionDate;
import com.taru.model.Pair;
import com.taru.utils.DateUtils;
import com.taru.utils.NumbersUtils;

public class PinkiCenter {

  public static final int NUMBER_OF_DAYS_INT_WEEK = 7;
  public static final double DOUBLE_ZERO = 0.0;
  public static final int WEEK_TOTAL_INDEX = 7;
  public static final int CYCLE_TOTAL_INDEX = 8;
  public static final int WEEK_TOTAL_PERCENTAGE_INDEX = 9;
  public static final int WEEK_LAST_INDEX = 6;

  public double getProjected(List<Pair<TransactionDate, Double>> values, int cleaningParam) {
    double res = 0;
    // Convert monthly total to per day each month amount
    List<Double> perDay = createListPerMonthlyDays(values);
    // Clean list from irregular values
    removeSDFromList(perDay, cleaningParam);
    // get the forecast value
    res = getForecastValue(perDay);
    return res;
  }

  /**
   * @param perDay
   * @return
   */
  public double getForecastValue(List<Double> perDay) {
    double res;
    // create table for projection
    double[][] table = createAvrageTable(perDay);
    // Calculate projected value
    //res = getMinMad(table);
    res = getAvaregeUsingWeigth(table);
    return res;
  }

  public void createWeeklyProjected(List<Transaction> transactions, int numberOfWeeksInCycle) {
    // Fill table with transactions and weekly totals
    List<Double[]> weeksTable = generateWeeksTable(transactions, numberOfWeeksInCycle);
    // Fix weekly totals which are out of range
    fixAnormalWeeks(weeksTable, numberOfWeeksInCycle);
    // Calculate cycle totals and weekly percentage per cycle
    int startIndex = calculateCycleTotalsAndPercentages(weeksTable, numberOfWeeksInCycle);

    // create weekly percentage table

    // calculate projected weeks (how much to produce) with percentage

  }

  /**
   * Function which generates table of weeks (sunday till sunday)
   * with days of transactions, total for each week, and total for each 4 months
   *
   * @param transactions
   * @return
   */
  public List<Double[]> generateWeeksTable(List<Transaction> transactions, int numberOfWeeksInCicle) {

    List<Double[]> weeks = new LinkedList<>();

    int weekNumber = 0;
    int startDayOfWeek = 0;
    TransactionDate startDate;

    double totalWeek = 0;

    Transaction firstTransaction = transactions.get(0);
    startDayOfWeek = DateUtils.dayOfWeek(firstTransaction.getTransactionDate());
    startDate = firstTransaction.getTransactionDate();
    Iterator<Transaction> iterator = transactions.iterator();
    while (iterator.hasNext()) {
      Transaction transaction = iterator.next();
      Double[] transactionsForWeek;

      int dayOfWeek = DateUtils.dayOfWeek(transaction.getTransactionDate());
      int intervalToNextDay = DateUtils.dayBetween(startDate, transaction.getTransactionDate());

      if (startDayOfWeek + intervalToNextDay < WEEK_LAST_INDEX) { // same week
        transactionsForWeek = getWeek(weeks, weekNumber);
      } else { // start new week
        // update new start day
        startDayOfWeek = dayOfWeek;
        startDate = transaction.getTransactionDate();
        transactionsForWeek = weeks.get(weekNumber);
        // update total week
        totalWeek = NumbersUtils.round(totalWeek, 2);
        transactionsForWeek[WEEK_TOTAL_INDEX] = totalWeek;
        totalWeek = 0;

        // check if needs to add empty weeks
        intervalToNextDay = intervalToNextDay - (WEEK_LAST_INDEX - startDayOfWeek) - dayOfWeek;

        int numberOfEmtyWeeksToAdd = intervalToNextDay / NUMBER_OF_DAYS_INT_WEEK;
        if (intervalToNextDay > 0) {
          addEmptyWeeks(weeks, weekNumber, numberOfEmtyWeeksToAdd);
        }
        weekNumber += numberOfEmtyWeeksToAdd + 1;
        transactionsForWeek = getWeek(weeks, weekNumber);
      }

      // if amount is not initialized then do so
      if (transactionsForWeek[dayOfWeek] == null) {
        transactionsForWeek[dayOfWeek] = DOUBLE_ZERO;
      }
      // add amount to day
      transactionsForWeek[dayOfWeek] += transaction.getAmount();
      // add amount to total week calculation
      totalWeek += transaction.getAmount();

    }

    Double[] lastWeek = weeks.get(weekNumber);
    // update total week
    lastWeek[WEEK_TOTAL_INDEX] = totalWeek;

    return weeks;

  }

  public int calculateCycleTotalsAndPercentages(List<Double[]> weeks, int numberOfWeeksInCycle) {
    int firstIndex = 0;
    boolean stop = false;

    for (int j = weeks.size() - 1; j >= 0; j--) { // iterate over the cycles
      double totalCycle = 0;
      // calculate the total for each cycle
      for (int i = 0; i < numberOfWeeksInCycle; i++) {
        if (j - i < 0) { // we are out of bounce, need to ignore this weeks
          stop = true;
          break;
        }
        Double[] week = weeks.get(j - i);
        totalCycle += week[WEEK_TOTAL_INDEX];
      }
      if (stop) {
        break;
      }
      Double[] lastWeekOfCycle = weeks.get(j);
      lastWeekOfCycle[CYCLE_TOTAL_INDEX] = totalCycle;
      // for each element in this cycle, calculate its percentage
      for (int i = 0; i < numberOfWeeksInCycle; i++) {
        if (j - i < 0) { // we are out of bounce, need to ignore this weeks
          stop = true;
          break;
        }
        Double[] week = weeks.get(j - i);
        week[WEEK_TOTAL_PERCENTAGE_INDEX] = NumbersUtils.round(week[WEEK_TOTAL_INDEX] / totalCycle, 2);
      }
      if (stop) {
        break;
      }
      j = j - numberOfWeeksInCycle + 1; // plus 1 because than we do j-- again
      firstIndex = j;
    }

    return firstIndex;
  }

  public void fixAnormalWeeks(List<Double[]> weeks, int numberOfWeeksInCycle) {
    for (int i = 0; i < numberOfWeeksInCycle; i++) {
      List<Double> values = getWeekiValues(weeks, i, numberOfWeeksInCycle);
      double mean = NumbersUtils.mean(values);
      double sd = NumbersUtils.SD(values, mean);
      int j = i;
      Iterator<Double> iterator = values.iterator();
      while (iterator.hasNext()) {
        Double next = iterator.next();
        Double[] transactions = weeks.get(j);
        if (next > (mean + 2 * sd)) {
          transactions[WEEK_TOTAL_INDEX] = mean + 2 * sd;
        } else if (next < (mean - 2 * sd)) {
          transactions[WEEK_TOTAL_INDEX] = mean - 2 * sd;
        }
        j += 4;
      }
    }
  }

  private List<Double> getWeekiValues(List<Double[]> weeks, int startIndex, int numberOfWeeksInCycle) {
    List<Double> result = new LinkedList<>();
    try {
      while (startIndex < weeks.size()) {
        Double[] week = weeks.get(startIndex);
        result.add(week[WEEK_TOTAL_INDEX]);
        startIndex += numberOfWeeksInCycle;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  private void addEmptyWeeks(List<Double[]> weeks, int weekNumber, int numberOfEmptyWeeksToAdd) {
    int iterateIndex = weekNumber + 1;
    for (int i = 0; i < numberOfEmptyWeeksToAdd; i++) {
      weeks.add(iterateIndex, new Double[NUMBER_OF_DAYS_INT_WEEK + 3]);
    }
  }

  private Double[] getWeek(List<Double[]> weeks, int weekNumber) {
    Double[] transactionsForWeek;
    if (weeks.size() <= weekNumber) {
      transactionsForWeek = new Double[NUMBER_OF_DAYS_INT_WEEK + 3];
      weeks.add(weekNumber, transactionsForWeek);
    } else {
      transactionsForWeek = weeks.get(weekNumber);
    }
    return transactionsForWeek;
  }

  private double getAvaregeUsingWeigth(double[][] table) {
    return table[table.length - 1][2];
  }

  private List<Double> createListPerMonthlyDays(List<Pair<TransactionDate, Double>> values) {
    List<Double> perDay = new LinkedList<>();
    Iterator<Pair<TransactionDate, Double>> iter = values.iterator();
    while (iter.hasNext()) {
      Pair<TransactionDate, Double> pair = iter.next();
      int month = pair.getFirst().getMonth();
      int year = pair.getFirst().getYear();
      int daysPerMonth = DateUtils.getNumberOfDays(month, year);
      double amountPerDay = pair.getSecond() / daysPerMonth;
      amountPerDay = NumbersUtils.round(amountPerDay, 2);
      perDay.add(amountPerDay);

    }

    return perDay;

  }

  private void removeSDFromList(List<Double> values, int count) {
    double mean = NumbersUtils.mean(values);
    double sd = NumbersUtils.SD(values, mean);
    sd = NumbersUtils.round(sd, 1);
    Iterator<Double> iter = values.iterator();
    while (iter.hasNext()) {
      Double val = iter.next();

      if ((val > (mean + count * sd)) || (val < (mean - count * sd))) {
        iter.remove();
      }
    }
  }

  private double[][] createAvrageTable(List<Double> months) {
    if (months == null || months.size() < 4) {
      return null;
    }

    double res[][] = new double[months.size() + 1][4];

    // Fill basic
    for (int i = 0; i < months.size(); i++) {
      res[i][0] = months.get(i);
    }

    for (int i = 3; i < months.size() + 1; i++) {
      res[i][1] = (res[i - 1][0] + res[i - 2][0] + res[i - 3][0]) / 3;
      res[i][2] = (0.2 * res[i - 3][0]) + (0.3 * res[i - 2][0]) + (0.5 * res[i - 1][0]);
    }

    return res;
  }

  private double getMadOfAverage(double[][] arr) {
    return getMad(arr, 1);
  }

  private double getMadOfWeigthAverage(double[][] arr) {
    return getMad(arr, 2);
  }

  private double getMad(double[][] arr, int index) {

    double sumDeviation = 0;
    int j = 3;
    for (int i = 0; i < 3; i++) {
      double deviation = arr[j][0] - arr[j][index];
      deviation = NumbersUtils.round(deviation, 2);
      double absDeviation = Math.abs(deviation);
      sumDeviation += absDeviation;
      j++;
    }
    return sumDeviation / 3;

  }

  private double getMinMad(double[][] table) {
    double averageMad = getMadOfAverage(table);
    double wiegthAverage = getMadOfWeigthAverage(table);
    return Math.min(averageMad, wiegthAverage);
  }
}

