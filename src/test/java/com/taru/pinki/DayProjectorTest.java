package com.taru.pinki;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;

/**
 * Created by Shiran Maor on 10/29/2015.
 */
public class DayProjectorTest extends TestCase {

  @Test
  public void testMostFrequent() throws Exception {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(3, 4);
    map.put(1, 1);
    map.put(4, 10);
    map.put(2, 7);
    map.put(6, 2);
    int res = DaysProjector.mostFrequentDay(map);
    assertEquals(4, res);
  }

  @Test
  public void testMostFrequent1() throws Exception {
    Map<Integer, Integer> map = new TreeMap<>();
    map.put(3, 4);
    map.put(1, 1);
    map.put(4, 10);
    map.put(2, 7);
    map.put(PinkiCenter.NO_MORE_DAYS_MARK, 10);
    int res = DaysProjector.mostFrequentDay(map);
    assertEquals(PinkiCenter.NO_MORE_DAYS_MARK, res);
  }

  @Test
  public void testMostFrequent2() throws Exception {
    Map<Integer, Integer> map = new TreeMap<>();
    int res = DaysProjector.mostFrequentDay(map);
    assertEquals(-1, res);
  }

  @Test
  public void testAddFrequencyToMap() throws Exception {
    Map<Integer, Integer> map = new TreeMap<>();
    map.put(3, 4);
    map.put(1, 1);
    map.put(4, 10);

    DaysProjector.addFrequencyToMap(map, 4);
    Integer res = map.get(4);
    assertEquals(11, res.intValue());
  }

  @Test
  public void testAddFrequencyToMap1() throws Exception {
    Map<Integer, Integer> map = new TreeMap<>();
    map.put(3, 4);
    map.put(1, 1);
    map.put(4, 10);

    DaysProjector.addFrequencyToMap(map, 2);
    Integer res = map.get(2);
    assertEquals(1, res.intValue());
  }

  @Test
  public void testPopulateMapWeekFrequencies() throws Exception {
    Double[] week = {null, 20.0, 10.0, null, 10.0, null, 30.0};
    Map<Integer, Integer> map = new TreeMap<>();
    boolean res = DaysProjector.populateMapWeekFrequencies(0, map, week);
    assertEquals(false, res);
  }

  @Test
  public void testPopulateMapWeekFrequencies1() throws Exception {
    Double[] week = {null, null, null, null, 10.0, null, 30.0};
    Map<Integer, Integer> map = new TreeMap<>();
    boolean res = DaysProjector.populateMapWeekFrequencies(0, map, week);
    assertEquals(false, res);
  }

  @Test
  public void testPopulateMapWeekFrequencies2() throws Exception {
    Double[] week = {null, null, null, null, 10.0, null, 30.0};
    Map<Integer, Integer> map = new TreeMap<>();
    boolean res = DaysProjector.populateMapWeekFrequencies(4, map, week);
    assertEquals(false, res);
  }

  @Test
  public void testCreateFrequencyMapForNextDayForWeeksI() throws Exception {
    Double[] week1 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week2 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week3 = {null, 1.0, 1.0, null, null, 1.0, null};

    List<Double[]> weeks = new LinkedList<>();
    weeks.add(week1);
    weeks.add(week2);
    weeks.add(week3);

    int weekIterator = 0;
    int lasttransactionDay = 0;
    int numberOfWeeksInCycle = 1;

    Map<Integer, Integer> frequencyMapForNextDayForWeeksI = DaysProjector.createFrequencyMapForNextDayForWeeksI(weeks, weekIterator, lasttransactionDay, numberOfWeeksInCycle);
    Integer frequencyOfMonday = frequencyMapForNextDayForWeeksI.get(1);
    assertEquals(3, frequencyOfMonday.intValue());
  }

  @Test
  public void testCreateFrequencyMapForNextDayForWeeksI1() throws Exception {
    Double[] week1 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week2 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week3 = {null, 1.0, 1.0, null, null, 1.0, null};

    List<Double[]> weeks = new LinkedList<>();
    weeks.add(week1);
    weeks.add(week2);
    weeks.add(week3);

    int weekIterator = 0;
    int lasttransactionDay = 2;
    int numberOfWeeksInCycle = 1;

    Map<Integer, Integer> frequencyMapForNextDayForWeeksI = DaysProjector.createFrequencyMapForNextDayForWeeksI(weeks, weekIterator, lasttransactionDay, numberOfWeeksInCycle);
    Integer frequencyOfMonday = frequencyMapForNextDayForWeeksI.get(1);
    assertEquals(true, frequencyOfMonday == null);
  }

  @Test
  public void testCreateFrequencyMapForNextDayForWeeksI2() throws Exception {
    Double[] week1 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week2 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week3 = {null, 1.0, 1.0, null, null, 1.0, null};

    List<Double[]> weeks = new LinkedList<>();
    weeks.add(week1);
    weeks.add(week2);
    weeks.add(week3);

    int weekIterator = 0;
    int lasttransactionDay = 2;
    int numberOfWeeksInCycle = 1;

    Map<Integer, Integer> frequencyMapForNextDayForWeeksI = DaysProjector.createFrequencyMapForNextDayForWeeksI(weeks, weekIterator, lasttransactionDay, numberOfWeeksInCycle);
    Integer frequencyOfMonday = frequencyMapForNextDayForWeeksI.get(5);
    assertEquals(3, frequencyOfMonday.intValue());
  }

  @Test
  public void testCreateFrequencyMapForNextDayForWeeksI3() throws Exception {
    Double[] week1 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week2 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week3 = {null, 1.0, 1.0, null, null, 1.0, null};

    List<Double[]> weeks = new LinkedList<>();
    weeks.add(week1);
    weeks.add(week2);
    weeks.add(week3);

    int weekIterator = 0;
    int lastTransactionDay = 5;
    int numberOfWeeksInCycle = 1;

    Map<Integer, Integer> frequencyMapForNextDayForWeeksI = DaysProjector.createFrequencyMapForNextDayForWeeksI(weeks, weekIterator, lastTransactionDay, numberOfWeeksInCycle);
    Integer frequencyOfMonday = frequencyMapForNextDayForWeeksI.get(PinkiCenter.NO_MORE_DAYS_MARK);
    assertEquals(3, frequencyOfMonday.intValue());
  }

  @Test
  public void testFindNextMostFrequentForWeeksI() throws Exception {
    Double[] week1 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week2 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week3 = {null, 1.0, 1.0, null, null, 1.0, null};

    List<Double[]> weeks = new LinkedList<>();
    weeks.add(week1);
    weeks.add(week2);
    weeks.add(week3);

    int weekIterator = 0;
    int lastTransactionDay = 0;
    int numberOfWeeksInCycle = 1;

    int nextMostFrequentForWeeksI = DaysProjector.findNextMostFrequentForWeeksI(weeks, weekIterator, lastTransactionDay, numberOfWeeksInCycle);
    int actual = 1;
    assertEquals(actual, nextMostFrequentForWeeksI);
  }

  @Test
  public void testFindNextMostFrequentForWeeksI1() throws Exception {
    Double[] week1 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week2 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week3 = {null, null, 1.0, null, null, 1.0, null};

    List<Double[]> weeks = new LinkedList<>();
    weeks.add(week1);
    weeks.add(week2);
    weeks.add(week3);

    int weekIterator = 0;
    int lastTransactionDay = 0;
    int numberOfWeeksInCycle = 1;

    int nextMostFrequentForWeeksI = DaysProjector.findNextMostFrequentForWeeksI(weeks, weekIterator, lastTransactionDay, numberOfWeeksInCycle);
    int actual = 1;
    assertEquals(actual, nextMostFrequentForWeeksI);
  }

  @Test
  public void testFindNextMostFrequentForWeeksI2() throws Exception {
    Double[] week1 = {null, 1.0, 1.0, null, null, 1.0, null};
    Double[] week2 = {null, null, 1.0, null, null, 1.0, null};
    Double[] week3 = {null, null, 1.0, null, null, 1.0, null};

    List<Double[]> weeks = new LinkedList<>();
    weeks.add(week1);
    weeks.add(week2);
    weeks.add(week3);

    int weekIterator = 0;
    int lastTransactionDay = 0;
    int numberOfWeeksInCycle = 1;

    int nextMostFrequentForWeeksI = DaysProjector.findNextMostFrequentForWeeksI(weeks, weekIterator, lastTransactionDay, numberOfWeeksInCycle);
    int actual = 2;
    assertEquals(actual, nextMostFrequentForWeeksI);
  }

  @Test
  public void testFindNextMostFrequentForWeeksI3() throws Exception {
    Double[] week1 = {null, 1.0, 1.0, 1.0, null, 1.0, null};
    Double[] week2 = {null, null, null, 1.0, null, null, null};
    Double[] week3 = {null, null, 1.0, null, null, 1.0, null};

    List<Double[]> weeks = new LinkedList<>();
    weeks.add(week1);
    weeks.add(week2);
    weeks.add(week3);

    int weekIterator = 0;
    int lastTransactionDay = 3;
    int numberOfWeeksInCycle = 1;

    int nextMostFrequentForWeeksI = DaysProjector.findNextMostFrequentForWeeksI(weeks, weekIterator, lastTransactionDay, numberOfWeeksInCycle);
    int actual = PinkiCenter.NO_MORE_DAYS_MARK;
    assertEquals(actual, nextMostFrequentForWeeksI);
  }


}
