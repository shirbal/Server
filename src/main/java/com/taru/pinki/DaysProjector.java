package com.taru.pinki;

import com.taru.model.Pair;
import com.taru.model.TransactionDate;
import com.taru.model.enums.Week;
import com.taru.utils.NumbersUtils;

import java.util.*;

/**
 * Created by Shiran Maor on 10/25/2015.
 */
public class DaysProjector {

  /**
   *
   * @param weeks
   * @param calculateWeeklyProjected
   * @param startIndex
   * @param numberOfWeeksInCycle
   * @return
   */
  public static LinkedList<Pair<Integer,Double>>[] createProjectedAmountsPerDay(List<Double[]> weeks, double[] calculateWeeklyProjected,
                                                             int startIndex, int numberOfWeeksInCycle) {
    // create dates distribution
    LinkedList<Pair<Integer,Double>>[] result ;
    try {
      List<Integer>[] daysDistribution = createDaysDistribution(weeks, startIndex, numberOfWeeksInCycle);
      if (daysDistribution != null && daysDistribution.length > 0) {
        result = new LinkedList[daysDistribution.length];
        try {
          int length = WeeklyProjector.getNumberOfWeeksToCompare(weeks, numberOfWeeksInCycle, startIndex);
          for (int weeksIterator = 0; weeksIterator < numberOfWeeksInCycle; weeksIterator++) {

            int firstWeekPointer = startIndex + weeksIterator; // point to first week of the weeks series to compare
            Map<Integer, Pair<Integer, Double>> dayPercents = calculateDaysPercents(weeks, numberOfWeeksInCycle, length, firstWeekPointer);
            calculatePricePerDay(calculateWeeklyProjected[weeksIterator], daysDistribution[weeksIterator], result, weeksIterator, dayPercents);

          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      else { // Initialize empty result
        result = initializeEmptyLinkedList();
      }
    } catch (Exception e) {
      // LOG this
      result = initializeEmptyLinkedList();
    }
    return result;
  }

  /**
   *
   * @param weeks
   * @param numberOfWeeksInCycle
   * @param length
   * @param firstWeekPointer
   * @return
   */
  private static Map<Integer, Pair<Integer, Double>> calculateDaysPercents(List<Double[]> weeks,
                                                          int numberOfWeeksInCycle, int length, int firstWeekPointer) {
    Map<Integer,Pair<Integer,Double>> dayPercents = new HashMap<>();

    for(int i = firstWeekPointer; i <= length*numberOfWeeksInCycle; i +=numberOfWeeksInCycle) { // iterate over all weeks i
      Double[] week = weeks.get(i);
      for(int day = 0; day <= PinkiCenter.WEEK_LAST_INDEX; day++) {
        if(week[day] != null) {
          Pair<Integer, Double> pair = dayPercents.get(day); // get for day it's total and total percents so far
          if(pair == null) {
            pair = new Pair<>();
          }

          int previousFirstValue = (pair.getFirst() != null) ? pair.getFirst() : 0 ;
          pair.setFirst(previousFirstValue + 1);

          double dayPercent = week[day] / week[PinkiCenter.WEEK_TOTAL_INDEX];
          dayPercent = NumbersUtils.round(dayPercent, 2);

          double previousSecondValue = (pair.getSecond() != null) ? pair.getSecond() : 0 ;
          pair.setSecond(previousSecondValue + dayPercent);
          dayPercents.put(day,pair);
        }
      }
    } // finished iterating over weeks i
    return dayPercents;
  }

  /**
   *
   * @param totalForWeek
   * @param days
   * @param result
   * @param iter
   * @param dayPercents
   */
  private static void calculatePricePerDay(double totalForWeek, List<Integer> days,  LinkedList<Pair<Integer,Double>>[] result,
                                            int iter, Map<Integer, Pair<Integer, Double>> dayPercents) {

    result[iter] = new LinkedList<>();
    double totalPercentsForThisWeek = calculateTotalPercentsForWeekI(days, dayPercents);
    Iterator<Integer> iterator1 = days.iterator();
    while(iterator1.hasNext()) {
      Integer day = iterator1.next();
      double price = calculatePriceForDayI(totalForWeek, dayPercents, totalPercentsForThisWeek, day);
      result[iter].add(new Pair<>(day,price));
    }

  }

  private static double calculatePriceForDayI(double totalForWeek, Map<Integer, Pair<Integer, Double>> dayPercents,
                                              double totalPercentsForThisWeek, Integer day) {
    double price  = 0;
    Pair<Integer, Double> dayPercentsSumAndTotal = dayPercents.get(day);
    if (dayPercentsSumAndTotal != null) {
      double averagePercentToDay = dayPercentsSumAndTotal.getSecond() / dayPercentsSumAndTotal.getFirst();
      double averagePercentToDayFixed = averagePercentToDay / totalPercentsForThisWeek;
      price = averagePercentToDayFixed * totalForWeek;
      price = NumbersUtils.round(price, 2);
    }
    return price;
  }

  private static double calculateTotalPercentsForWeekI(List<Integer> days, Map<Integer, Pair<Integer, Double>> dayPercents) {
    double totalPercentsForThisWeek = 0;
    Iterator<Integer> iterator = days.iterator();
    while(iterator.hasNext()) {
      Integer day = iterator.next();
      totalPercentsForThisWeek += calculateWeekIPercentage(dayPercents, day);
    }
    return totalPercentsForThisWeek;
  }

  private static double calculateWeekIPercentage(Map<Integer, Pair<Integer, Double>> dayPercents, Integer day) {
    double result = 0;
    Pair<Integer, Double> dayPercentsSumAndTotal = dayPercents.get(day);
    if (dayPercentsSumAndTotal != null) {
      result = dayPercentsSumAndTotal.getSecond() / dayPercentsSumAndTotal.getFirst();
    }
    return result;
  }

  /**
   * Creates a list of days divided to weeks ('numberOfWeeksInCycle' in number) which are the most frequent days of
   * transactions from all weeks
   * @param weeks
   *    Transaction tables by weeks
   * @param startIndex
   *    The first week to scan from weeks table
   * @param numberOfWeeksInCycle
   *    The number of weeks in each cycle of search
   * @return
   *    Array of days which are the most frequent days
   */
  public static List<Integer>[] createDaysDistribution(List<Double[]> weeks, int startIndex, int numberOfWeeksInCycle) {
    List<Integer>[] result = initializeDayDistributionLists(numberOfWeeksInCycle);

    try {
      for (int resultIterator = 0; resultIterator < numberOfWeeksInCycle; resultIterator++) {
        List<Integer> weekIResult = result[resultIterator];
        boolean continueLooking = true;
        int lastTransactionDay = 0;

        int weekIterator = startIndex + resultIterator; // point to first week of the weeks series to compare
        while (continueLooking) {
          // find next most frequent day
          int mostFrequentDay = findNextMostFrequentForWeeksI(weeks, weekIterator, lastTransactionDay, numberOfWeeksInCycle);

          /**
           * If the most frequent day is not the end of the week (meaning no mor days of transactions)
           * add this day to the result on week 'resultIterator' and set the 'lastTransactionDay' with
           * the last frequent day which was chosen.
           */
          if (mostFrequentDay != PinkiCenter.NO_MORE_DAYS_MARK) {
            weekIResult.add(mostFrequentDay);
            lastTransactionDay = mostFrequentDay;
          }
          /**
           * If the next day is last day of week of the most frequent day is 'NO_MORE_DAYS_MARK' than we are done.
           */
          if (Week.lastDayOfWeek(lastTransactionDay + 1) || mostFrequentDay == PinkiCenter.NO_MORE_DAYS_MARK) { // end of week or no more of days of transactions
            continueLooking = false;
          }
        }
      }
    } catch (Exception e) {
      // LOG this
    }

    return result;
  }

  /**
   * Finds the next most frequent day with transaction starting from week 'weekIterator' and from day 'lastTransactionDay'
   * for each week which is being scanned.
   * @param weeks
   *    Transaction tables by weeks
   * @param weekIterator
   *    The first week index to start from
   * @param lastTransactionDay
   *    The first day in each week to start from
   * @param numberOfWeeksInCycle
   *    The number of weeks in each cycle of search
   * @return
   *    The next most frequent day
   */
  public static int findNextMostFrequentForWeeksI(List<Double[]> weeks, int weekIterator, int lastTransactionDay,
                                                   int numberOfWeeksInCycle) {
    int mostFrequentDay = -1;
    try {
      Map<Integer, Integer> frequencyMap = createFrequencyMapForNextDayForWeeksI(weeks, weekIterator, lastTransactionDay, numberOfWeeksInCycle);
      mostFrequentDay = mostFrequentDay(frequencyMap);
    } catch (Exception e) {
      // LOG this
    }
    return mostFrequentDay;
  }

  /**
   * Creates a map of day's frequency. Fill the map with days from each 'numberOfWeeksInCycle' starting from 'weekIterator' week
   * and starting in each week from 'lastTransactionDay' day.
   * @param weeks
   *    Transaction tables by weeks
   * @param weekIterator
   *    The first week index to start from
   * @param lastTransactionDay
   *    The first day in each week to start from
   * @param numberOfWeeksInCycle
   *    The number of weeks in each cycle of search
   * @return
   *    Map<Day:Frequency>: Hash map for days and it's frequency
   */
  public static Map<Integer, Integer> createFrequencyMapForNextDayForWeeksI(List<Double[]> weeks, int weekIterator,
                                                                            int lastTransactionDay, int numberOfWeeksInCycle) {
    Map<Integer, Integer> frequencyMap = new HashMap<>(); // for every week have its frequency table
    try {
      /** Iterate over the week every 'numberOfWeeksInCycle' week */
      while(weekIterator < weeks.size())  {
        Double[] week = weeks.get(weekIterator); // get week 'weekIterator'

        boolean hadTransactionOnPreviousDay = hadTransactionOnPreviousDay(week,lastTransactionDay);
        boolean firstDayOfWeek = Week.firstDayOfWeek(lastTransactionDay);
        /**
         * Either it is the begging of the week or this week had a transaction on 'lastTransactionDay'.
         * If true on one of the above, check this week for it's next transaction
         */
        boolean hadTransactionOnPreviousFrequentDay = hadTransactionOnPreviousDay || firstDayOfWeek;
        /**
         * If this week did not made transaction on previous frequent day
         * then don't take that week into consideration
         */
        if (hadTransactionOnPreviousFrequentDay) {
          boolean noMoreDaysAfterLastTransactionDay = populateMapWeekFrequencies(lastTransactionDay, frequencyMap, week);
          /**
           * In case this week has not more days of transaction add the  'NO_MORE_DAYS_MARK' as a "day" to the frequency map.
           * Add the  'NO_MORE_DAYS_MARK' only if this week had a transaction on  'lastTransactionDay', otherwise
           * don't count this week in the frequency
           */
          if (noMoreDaysAfterLastTransactionDay) { // check if this is the end of the week,
            // no more transactions for this week
            addFrequencyToMap(frequencyMap, PinkiCenter.NO_MORE_DAYS_MARK);
          }
        }
        /** Go to next week in the cycle */
        weekIterator += numberOfWeeksInCycle;
      }
    } catch (Exception e) {
      // LOG this
    }

    return frequencyMap;
  }

  /**
   * Check if the given week had a transaction on 'day'
   * @param week
   *        The week to check on
   * @param day
   *        the day to check if the week had a transaction on
   * @return
   *    True - The week had a transaction on 'day', False for otherwise
   */
  private static boolean hadTransactionOnPreviousDay(Double[] week, int day) {
    return week != null && week.length > day && week[day] != null;
  }

  /**
   *  Given a week 'week', populate  'frequencyMap' with this week
   * @param lastTransactionDay
   *        The day in the week which we previous had a transaction, starting to search from that day
   * @param frequencyMap
   *        Map of day's frequency to fill
   * @param week
   *        The week to iterate over
   * @return
   *  True, if this week is done. False otherwise
   */
  public static boolean populateMapWeekFrequencies(int lastTransactionDay, Map<Integer, Integer> frequencyMap, Double[] week ) {
    boolean noMoreDaysAfterLastTransactionDay = true;
    for (int k = lastTransactionDay + 1; k <= PinkiCenter.WEEK_LAST_INDEX; k++) { // iterate this week from last day to find next day

      if (week[k] != null) { // found next day for transaction from lastTransactionDay
        noMoreDaysAfterLastTransactionDay = false;
        addFrequencyToMap(frequencyMap, k);
        break;
      }
    }
    return noMoreDaysAfterLastTransactionDay;
  }

  /**
   * Update the freuqency map. Adds 1 if day already exists. If day does not exists it creates a new entry
   * in the map with frequency 1.
   * @param daysFreq
   *          Map<Day, Frequency> to update.
   * @param day
   *          The day to update its frequency
   */
  public static void addFrequencyToMap(Map<Integer, Integer> daysFreq, int day) {
    Integer freq = daysFreq.get(day);
    if (freq == null) {
      freq = 0;
    }
    freq += 1;
    daysFreq.put(day, freq);
  }

  /**
   * Returns the day which is the most frequent from all days in map
   * @param daysFreq
   *    Map<Day, Frequency> - contains the frequency of each day in a specific week
   * @return Integer, a day which is the more frequent from a list of days
   */
  public static int mostFrequentDay(Map<Integer, Integer> daysFreq) {
    if(daysFreq == null || daysFreq.size() == 0) {
      return -1;
    }
    int result = PinkiCenter.NO_MORE_DAYS_MARK;
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
        if (day != PinkiCenter.NO_MORE_DAYS_MARK) {
          if (result < day) {
            result = day;
          }
        } else {
          result = PinkiCenter.NO_MORE_DAYS_MARK;
        }
      }
    }
    return result;
  }

  /**
   * Initialize array of lists which will represents the weeks output
   * @param numberOfWeeksInCycle
   *        Who many weeks are in one cycle
   * @return  Array of List of inter. Each row represents a week, each list represents a list of day which
   *          will have transactions.
   */
  private static List<Integer>[] initializeDayDistributionLists(int numberOfWeeksInCycle) {
    int size = (numberOfWeeksInCycle < 0) ? 0 : numberOfWeeksInCycle;
    List<Integer>[] result = new LinkedList[size];
    for (int i = 0; i < numberOfWeeksInCycle; i++) {
      result[i] = new LinkedList<>();
    }
    return result;
  }

  /**
   * Initialize empty linked list. Being used for cases with error.
   * @return
   * Empty LinkedList
   */
  private static LinkedList[] initializeEmptyLinkedList() {
    return new LinkedList[0];
  }

  public static int getProjectedDay(List<Double> days) {
    int result = 0;
//    int total = 0;
//    Iterator<Double> iterator = days.iterator();
//    while(iterator.hasNext()) {
//      Double day = iterator.next();
//      total += day;
//    }
//    result = total/days.size();
    Map<Integer,Integer> frequency = new HashMap<>();
    Iterator<Double> iterator = days.iterator();
    while(iterator.hasNext()) {
      Double next = iterator.next();
      int day = next.intValue();
      Integer frequ = frequency.get(day);
      if(frequ == null) {
        frequ = 0;
      }
      frequ += 1;
      frequency.put(day,frequ);
    }
    result = DaysProjector.mostFrequentDay(frequency);
    return result;
  }

  private static boolean checkIfExistsLastThreeMonths(List<TransactionDate> dates) {
    return false;
  }
}
