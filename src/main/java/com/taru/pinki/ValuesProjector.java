package com.taru.pinki;

import com.taru.model.Pair;
import com.taru.utils.DateUtils;
import com.taru.utils.NumbersUtils;

import java.util.*;

/**
 * Created by Shiran Maor on 10/25/2015.
 */
public class ValuesProjector {

  public static double getProjectedForTransaction(List<Double> values,double sdFactor) {
    double res = 0;
    try {
      removeSDFromList(values, sdFactor);
      // get the forecast value
      res = getForecastValue(values);
    } catch (Exception e) {
      // LOG this
    }
    return res;
  }

  public static double getProjectedForDays(Map<Pair<Integer, Integer>, Double> values, int numberOfDaysProjection, double sdFactor) {
    double res = 0;
    // Convert monthly total to per day each month amount
    List<Double> perDay = createListPerMonthlyDays(values);
    // Clean list from irregular values
    removeSDFromList(perDay, sdFactor);
    // get the forecast value
    res = getForecastValue(perDay);
    res = res * numberOfDaysProjection;
    return res;
  }

  private static double[][] createAverageTable(List<Double> months) {
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

  private static double getMadOfAverage(double[][] arr) {
    return getMad(arr, 1);
  }

  private static double getMadOfWeightAverage(double[][] arr) {
    return getMad(arr, 2);
  }

  private static double getMad(double[][] arr, int index) {

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

  private static double getMinMad(double[][] table) {
    double averageMad = getMadOfAverage(table);
    double wiegthAverage = getMadOfWeightAverage(table);
    return Math.min(averageMad, wiegthAverage);
  }

  /**
   * @param perDay
   * @return
   */
  private static double getForecastValue(List<Double> perDay) {
    double res;
    // create table for projection
    double[][] table = createAverageTable(perDay);
    // Calculate projected value
    //res = getMinMad(table);
    res = getAverageUsingWeight(table);
    res = NumbersUtils.round(res,2);
    return res;
  }

  private static double getAverageUsingWeight(double[][] table) {
    return table[table.length - 1][2];
  }

  /**
   *
   * @param values
   * @return
   */
  private static List<Double> createListPerMonthlyDays(Map<Pair<Integer, Integer>, Double> values) {
    List<Double> perDay = new LinkedList<>();
    Set<Map.Entry<Pair<Integer, Integer>, Double>> entries = values.entrySet();
    Iterator<Map.Entry<Pair<Integer, Integer>, Double>> iterator = entries.iterator();
    while (iterator.hasNext()) {
      Map.Entry<Pair<Integer, Integer>, Double> pairDoubleEntry = iterator.next();
      Pair<Integer, Integer> yearMonth = pairDoubleEntry.getKey();
      int year = yearMonth.getFirst();
      int month = yearMonth.getSecond();
      int daysPerMonth = DateUtils.monthNumberOfDays(month, year);
      double amountPerDay = pairDoubleEntry.getValue() / daysPerMonth;
      amountPerDay = NumbersUtils.round(amountPerDay, 2);
      perDay.add(amountPerDay);

    }

    return perDay;

  }

  public static void removeSDFromList(List<Double> values, double sdFactor) {
    double mean = NumbersUtils.mean(values);
    double sd = NumbersUtils.SD(values, mean);
    sd = NumbersUtils.round(sd, 3);
    if (sd > 0) {
      Iterator<Double> iter = values.iterator();
      while (iter.hasNext()) {
        Double val = iter.next();

        if ((val > (mean + (sdFactor * sd))) || (val < (mean - (sdFactor * sd)))) {
          iter.remove();
        }
      }
    }
  }

}
