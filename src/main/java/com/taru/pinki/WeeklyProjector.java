package com.taru.pinki;

import java.util.List;

/**
 * Created by Shiran Maor on 10/25/2015.
 */
public class WeeklyProjector {

  /**
   *
   * @param weeks
   *          Table with all transaction divided to numberOfWeeksInCycle
   * @param totalProjected
   *          Total amount which is projected
   * @param startIndex
   *          The first row index in 'weeks'
   * @param numberOfWeeksInCycle
   * @return
   */
  public static double[] calculateWeeklyProjected(List<Double[]> weeks, double totalProjected,
                                                  int startIndex, int numberOfWeeksInCycle) {
    double[] res = new double[numberOfWeeksInCycle];
    // create weekly percentage table
    double[] weeklyPercentage = createWeeklyPercentage(weeks, numberOfWeeksInCycle, startIndex);
    for (int i = 0; i < weeklyPercentage.length; i++) {
      res[i] = weeklyPercentage[i] * totalProjected;
    }
    return res;
  }

  /**
   *
   * @param weeks
   * @param numberOfWeeksInCycle
   * @param startIndex
   * @return
   */
  private static double[] createWeeklyPercentage(List<Double[]> weeks, int numberOfWeeksInCycle, int startIndex) {
    int length = getNumberOfWeeksToCompare(weeks, numberOfWeeksInCycle, startIndex);
    double[] res = new double[numberOfWeeksInCycle];  // last row is the average
    for (int j = 0; j < numberOfWeeksInCycle; j++) {
      int iter = startIndex + j;
      double totalCount = 0;
      for (int i = 0; i < length; i++) {
        Double[] week = weeks.get(iter);
        if ( week[PinkiCenter.WEEK_TOTAL_PERCENTAGE_INDEX] != null) {
          totalCount += week[PinkiCenter.WEEK_TOTAL_PERCENTAGE_INDEX];
        }
        iter += numberOfWeeksInCycle;
      }
      res[j] = totalCount / length; // calculate the average for week j
    }
    return res;
  }

  /**
   *
   * @param weeks
   * @param numberOfWeeksInCycle
   * @param startIndex
   * @return
   */
  public static int getNumberOfWeeksToCompare(List<Double[]> weeks, int numberOfWeeksInCycle, int startIndex) {
    int result = (weeks.size() - startIndex) / numberOfWeeksInCycle;
    return result;
  }

}
