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
  public static final int NO_MORE_DAYS_MARK = 7;


  /**
   *
   * @param transactions
   * @param numberOfDaysProjection
   * @param numberOfWeeksInCycle
   * @param sdFactor
   * @return
   */
  public static List<Pair<Integer, Double>>[] createWeeklyProjected(List<Transaction> transactions,int numberOfDaysProjection,
                                                           int numberOfWeeksInCycle, double sdFactor) {

    List<Pair<Integer, Double>>[] result = null;
    // calculate projected
    Map<Pair<Integer, Integer>, Double> monthsTotal = createMonthsTotal(transactions);
    double totalProjected = ValuesProjector.getProjectedForDays(monthsTotal, numberOfDaysProjection, sdFactor);
    // Fill table with transactions and weekly totals
    List<Double[]> weeksTable = generateWeeksTable(transactions);
    // Fix weekly totals which are out of range
    fixAbnormalWeeks(weeksTable);
    // Calculate cycle totals and weekly percentage per cycle
    int startIndex = calculateCycleTotalsAndPercentages(weeksTable, numberOfWeeksInCycle);
    // calculate projected weeks (how much to produce) with percentage
    double[] calculateWeeklyProjected =
        WeeklyProjector.calculateWeeklyProjected(weeksTable,totalProjected,startIndex,numberOfWeeksInCycle);
    // create days projected
    result =
        DaysProjector.createProjectedAmountsPerDay(weeksTable, calculateWeeklyProjected, startIndex,numberOfWeeksInCycle);
    return result;
  }

  /**
   *
   * @param transactions
   * @return
   */
  private static Map<Pair<Integer, Integer>, Double> createMonthsTotal(List<Transaction> transactions) {
    Map<Pair<Integer, Integer>, Double> result = new HashMap<>();
    Iterator<Transaction> transactionIterator = transactions.iterator();
    while(transactionIterator.hasNext()) {
      Transaction transaction = transactionIterator.next();
      int month = transaction.getTransactionDate().getMonth();
      int year = transaction.getTransactionDate().getYear();
      Pair<Integer, Integer> key = new Pair<>(year, month);
      Double monthTotal = result.get(key);
      if(monthTotal == null) {
        monthTotal = 0.0;
      }
      monthTotal += transaction.getAmount();
      monthTotal = NumbersUtils.round(monthTotal,2);
      result.put(key,monthTotal);
    }

    return result;
  }

  /**
   * Function which generates table of weeks (sunday till sunday)
   * with days of transactions, total for each week, and total for each 4 months
   *
   * @param transactions
   * @return
   */
  private static List<Double[]> generateWeeksTable(List<Transaction> transactions) {

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

  /**
   *
   * @param weeks
   * @param numberOfWeeksInCycle
   * @return
   */
  private static int calculateCycleTotalsAndPercentages(List<Double[]> weeks, int numberOfWeeksInCycle) {
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
        if (week[WEEK_TOTAL_INDEX] != null) {
          totalCycle += week[WEEK_TOTAL_INDEX];
        }
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
        if (week[WEEK_TOTAL_INDEX] != null) {
          week[WEEK_TOTAL_PERCENTAGE_INDEX] = NumbersUtils.round(week[WEEK_TOTAL_INDEX] / totalCycle, 2);
        }
      }
      if (stop) {
        break;
      }
      j = j - numberOfWeeksInCycle + 1; // plus 1 because than we do j-- again
      firstIndex = j;
    }

    return firstIndex;
  }

  /**
   *
   * @param weeks
   */
  private static void fixAbnormalWeeks(List<Double[]> weeks) {
    List<Double> values = getWeekiValues(weeks);
    double mean = NumbersUtils.mean(values);
    double sd = NumbersUtils.SD(values, mean);
    Iterator<Double[]> iterator = weeks.iterator();
    while (iterator.hasNext()) {
      Double[] transactions = iterator.next();
      if (transactions[WEEK_TOTAL_INDEX] != null) {
        if (transactions[WEEK_TOTAL_INDEX] > (mean + 2 * sd)) {
          transactions[WEEK_TOTAL_INDEX] = mean + 2 * sd;
        } else if (transactions[WEEK_TOTAL_INDEX] < (mean - 2 * sd)) {
          transactions[WEEK_TOTAL_INDEX] = mean - 2 * sd;
        }
      }
    }
  }

  /**
   *
   * @param weeks
   * @return
   */
  private static List<Double> getWeekiValues(List<Double[]> weeks) {
    List<Double> result = new LinkedList<>();
    Iterator<Double[]> iterator = weeks.iterator();
    while (iterator.hasNext()) {
      Double[] next = iterator.next();
      if (next[WEEK_TOTAL_INDEX] != null) {
        result.add(next[WEEK_TOTAL_INDEX]);
      }
    }
    return result;
  }

  /**
   *
   * @param weeks
   * @param weekNumber
   * @param numberOfEmptyWeeksToAdd
   */
  private static void addEmptyWeeks(List<Double[]> weeks, int weekNumber, int numberOfEmptyWeeksToAdd) {
    int iterateIndex = weekNumber + 1;
    for (int i = 0; i < numberOfEmptyWeeksToAdd; i++) {
      weeks.add(iterateIndex, new Double[NUMBER_OF_DAYS_INT_WEEK + 3]);
    }
  }

  /**
   *
   * @param weeks
   * @param weekNumber
   * @return
   */
  private static Double[] getWeek(List<Double[]> weeks, int weekNumber) {
    Double[] transactionsForWeek;
    if (weeks.size() <= weekNumber) {
      transactionsForWeek = new Double[NUMBER_OF_DAYS_INT_WEEK + 3];
      weeks.add(weekNumber, transactionsForWeek);
    } else {
      transactionsForWeek = weeks.get(weekNumber);
    }
    return transactionsForWeek;
  }

}

