package com.taru.pinki;

import com.taru.model.enums.Week;

import java.util.*;

/**
 * Created by Shiran Maor on 10/25/2015.
 */
public class DaysProjector {

  public static List<Integer>[] createProjectedAmountsPerDay(List<Double[]> weeks, List<Integer>[] daysDistribution,
                                                             double[] calculateWeeklyProjected, int startIndex) {
    List<Integer>[] result = new LinkedList[daysDistribution.length];

    return result;
  }

  public static List<Integer>[] createDaysDistribution(List<Double[]> weeks, int startIndex, int numberOfWeeksInCycle) {
    List<Integer>[] result = initializeDayDistributionLists(numberOfWeeksInCycle);

    try {
      int length = WeeklyProjector.getNumberOfWeeksToCompare(weeks, numberOfWeeksInCycle, startIndex);
      for (int iter = 0; iter < numberOfWeeksInCycle; iter++) {
        boolean continueLooking = true;
        int currentDay = 0;
        while (continueLooking) {
          int weekIterator = startIndex + iter; // point to first week of the weeks series to compare
          // find next most frequent day
          int mostFrequentDay = findNextMostFrequent(weeks, length, weekIterator, currentDay, numberOfWeeksInCycle);
          if (mostFrequentDay != PinkiCenter.NO_MORE_DAYS_MARK) {
            result[iter].add(mostFrequentDay);
            currentDay = mostFrequentDay;
          }
          if (currentDay + 1 == Week.SATURDAY.ordinal() || mostFrequentDay == PinkiCenter.NO_MORE_DAYS_MARK) { // end of week or no more of days of transactions
            continueLooking = false;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }

  private static int findNextMostFrequent(List<Double[]> weeks, int numberOfCycles, int weekIterator, int startDay, int numberOfWeeksInCycle) {
    Map<Integer, Integer> daysFreq = new HashMap<>(); // for every week have its frequency table
    int counter = 0;
    while (counter < numberOfCycles) { // iterate over the weeks on place 'iter'
      Double[] week = weeks.get(weekIterator); // get week 'weekIterator'

      boolean noMoreDays = true;
      boolean hadTransactionOnPreviousFrequentDay = week[startDay] != null || startDay == 0;
      if (hadTransactionOnPreviousFrequentDay) { // if this week did not made transaction on previous frequent day
        // then don't take that week into consideration
        for (int k = startDay + 1; k <= PinkiCenter.WEEK_LAST_INDEX; k++) { // iterate this week from last day to find next day

          if (week[k] != null) { // found next day for transaction from startDay
            noMoreDays = false;
            addFrequencyToMap(daysFreq, k);
            break;
          }
        }
      }
      if (noMoreDays && hadTransactionOnPreviousFrequentDay) { // check if this is the end of the week,
        // no more transactions for this week
        addFrequencyToMap(daysFreq, PinkiCenter.NO_MORE_DAYS_MARK);
      }

      weekIterator += numberOfWeeksInCycle;
      counter++;
    }
    int mostFrequentDay = mostFrequentDay(daysFreq);
    return mostFrequentDay;
  }

  private static void addFrequencyToMap(Map<Integer, Integer> daysFreq, int k) {
    Integer freq = daysFreq.get(k);
    if (freq == null) {
      freq = 0;
    }
    freq += 1;
    daysFreq.put(k, freq);
  }

  private static int mostFrequentDay(Map<Integer, Integer> daysFreq) {
    int result = -1;
    int max = 0;
    Set<Map.Entry<Integer, Integer>> entries = daysFreq.entrySet();
    for (Iterator<Map.Entry<Integer, Integer>> iterator = entries.iterator(); iterator.hasNext(); ) {
      Map.Entry<Integer, Integer> next = iterator.next();
      Integer freq = next.getValue();
      if (max < freq) {
        max = freq;
        result = next.getKey();
      } else if (max == freq) { // IMPORTANT!!! If I have equal freq I pick tha last one;
        Integer day = next.getKey();
        if (day != -1) {
          if (result < day) {
            result = day;
          }
        } else {
          result = -1;
        }
      }
    }


    return result;
  }

  private static List<Integer>[] initializeDayDistributionLists(int numberOfWeeksInCycle) {
    List<Integer>[] result = new LinkedList[numberOfWeeksInCycle];
    for (int i = 0; i < numberOfWeeksInCycle; i++) {
      result[i] = new LinkedList<>();
    }
    return result;
  }

}
