package com.taru.pinki;

import java.util.List;

/**
 * Created by Shiran Maor on 10/25/2015.
 */
public class WeeklyProjector {

  public static double[] calculateWeeklyProjected(double[] weeklyPercentage, int numberOfWeeksInCycle, double totalProjected) {
    double[] res = new double[numberOfWeeksInCycle];
    for (int i = 0; i < weeklyPercentage.length; i++) {
      res[i] = weeklyPercentage[i] * totalProjected;
    }
    return res;
  }

  public static double[] createWeeklyPercentage(List<Double[]> weeks, int numberOfWeeksInCycle, int startIndex) {
    int length = getNumberOfWeeksToCompare(weeks, numberOfWeeksInCycle, startIndex);
    double[] res = new double[numberOfWeeksInCycle];  // last row is the average
    for (int j = 0; j < numberOfWeeksInCycle; j++) {
      int iter = startIndex + j;
      int totalCount = 0;
      for (int i = 0; i < length; i++) {
        Double[] week = weeks.get(iter);
        totalCount += week[PinkiCenter.WEEK_TOTAL_PERCENTAGE_INDEX];
        iter += numberOfWeeksInCycle;
      }
      res[j] = totalCount / length; // calculate the average for week j
    }
    return res;
  }

  public static int getNumberOfWeeksToCompare(List<Double[]> weeks, int numberOfWeeksInCycle, int startIndex) {
    int result = (weeks.size() - startIndex) / numberOfWeeksInCycle;
    return result;
  }

}
