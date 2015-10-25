package com.taru;

import com.taru.model.TransactionDate;
import com.taru.parsers.DateParser;
import com.taru.utils.DateUtils;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;


public class DateUtilsTest extends TestCase {

  @Test
  public void testGetNumberOfDays() throws Exception {
    int res = DateUtils.getNumberOfDays(10,2015);
    assertEquals(31,res);
  }

  @Test
  public void testGetDayInWeek() throws Exception {
    int res = DateUtils.dayInWeek(10, 24, 2015);
    assertEquals(6,res);
  }

  @Test
  public void testGetNumOfDaysBetweenMonths() throws Exception {
    int res = DateUtils.getNumOfDaysBetweenMonths(10, 11, 2015);
    assertEquals(31,res);
  }

  @Test
  public void testGetNumOfDaysBetweenMonths1() throws Exception {
    int res = DateUtils.getNumOfDaysBetweenMonths(8, 11, 2015);
    assertEquals(92,res);
  }

  @Test
  public void testGetNumberOfDaysFromStartYearTillMonth() throws Exception {
    int res = DateUtils.getNumberOfDaysFromStartYearTillMonth(2,2016);
    assertEquals(31,res);
  }

  @Test
  public void testGetNumberOfDaysFromStartYearTillMonth1() throws Exception {
    int res = DateUtils.getNumberOfDaysFromStartYearTillMonth(3,2016);
    assertEquals(60,res);
  }

  @Test
  public void testGetNumOfDaysTillEndOfYear() throws Exception {
    int res = DateUtils.getNumOfDaysTillEndOfYear(11, 2015);
    assertEquals(61,res);
  }

  @Test
  public void testGetNumOfDaysTillEndOfMonth() throws Exception {
    int res = DateUtils.getNumOfDaysTillEndOfMonth(24, 10, 2015);
    assertEquals(7,res);
  }

  @Test
  public void testGetDayInWeek1() throws Exception {
    int month = 10;
    int day = 24;
    int year = 2015;
    int res = DateUtils.dayInWeek(month, day, year);
    Assert.assertEquals(6, res);
  }

  @Test
  public void testGetDayInWeek2() throws Exception {
    int month = 3;
    int day = 23;
    int year = 2015;
    int res = DateUtils.dayInWeek(month, day, year);
    Assert.assertEquals(1,res);
  }

  @Test
  public void testDayBetween() throws Exception {
    TransactionDate d1 = DateParser.parseTimestamp("24-OCT-2015");
    TransactionDate d2 = DateParser.parseTimestamp("23-OCT-2015");
    int res = DateUtils.dayBetween(d2, d1);
    assertEquals(1,res);
  }

  @Test
  public void testDayBetween1() throws Exception {
    TransactionDate d1 = DateParser.parseTimestamp("24-OCT-2015");
    TransactionDate d2 = DateParser.parseTimestamp("1-OCT-2015");
    int res = DateUtils.dayBetween(d2, d1);
    assertEquals(23,res);
  }

  @Test
  public void testDayBetween2() throws Exception {
    TransactionDate d1 = DateParser.parseTimestamp("1-OCT-2015");
    TransactionDate d2 = DateParser.parseTimestamp("1-SEP-2015");
    int res = DateUtils.dayBetween(d2, d1);
    assertEquals(29,res);
  }

  @Test
  public void testDayBetween3() throws Exception {
    TransactionDate d1 = DateParser.parseTimestamp("24-JAN-2016");
    TransactionDate d2 = DateParser.parseTimestamp("20-DEC-2015");
    int res = DateUtils.dayBetween(d2, d1);
    assertEquals(35,res);
  }

}