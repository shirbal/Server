package com.taru.utils;

import com.taru.model.TransactionDate;
import com.taru.model.enums.Month;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static int monthNumberOfDays(int month, int year) {
		int res = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		res = calendar.getActualMaximum(Calendar.DATE);
		return res;
	}

	/**
	 * Returns the index of the day in the week in 0..6 indexing
	 * @param month
	 * @param day
	 * @param year
	 * @return
	 */
	public static int dayInWeek(int month, int day, int year) {
		Calendar calendar = Calendar.getInstance();
		month--; // calender takes month in indexing of 0..11
		calendar.set(year,month,day);
		int result = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return result;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static int dayOfWeek(TransactionDate date) {
		int month = date.getMonth();
		int day = date.getDay();
		int year = date.getYear();
		int dayInWeek = DateUtils.dayInWeek(month, day, year);
		return dayInWeek;

	}

	/**
	 * Assuming date1 is smaller then date2
	 * @param date1  The first date
	 * @param date2  The second date
	 * @return The number of days between those two dates
	 */
	public static int daysBetween(TransactionDate date1, TransactionDate date2) {
		int result = 0;

		int year2 = date2.getYear();
		int month2 = date2.getMonth();
		int day2 = date2.getDay();

		int year1 = date1.getYear();
		int month1 = date1.getMonth();
		int day1 = date1.getDay();


		if(year2 == year1) { // same year

			if(month1 == month2) {
				result += day2 - day1;
			} else {
				result += getNumOfDaysTillEndOfMonth(day1,month1,year1);
				int nextMonth = month1 + 1;
				if (month2 > nextMonth) {
					result += getNumOfDaysBetweenMonths(nextMonth,month2,year1);
				}
				result += day2 - 1;
			}

		} else { // not same year
			result += getNumOfDaysTillEndOfMonth(day1,month1,year1) + 1;
			if (month1 < Month.DECEMBER.get()) {
				result += getNumOfDaysTillEndOfYear(month1+1,year1);
			}
			if (month2 > Month.JANUARY.get()) {
				result += getNumberOfDaysFromStartYearTillMonth(month2,year2);
			}
			result += day2 - 1;

		}


		return result;
	}

	/**
	 *
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getNumberOfDaysFromStartYearTillMonth(int month, int year) {
		int result ;
		if (month > Month.JANUARY.get()) {
			result = getNumOfDaysBetweenMonths(Month.JANUARY.get(), month, year);
		} else {
			result = monthNumberOfDays(Month.JANUARY.get(), year);
		}
		return result;
	}

	/**
	 *
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getNumOfDaysTillEndOfYear(int month, int year) {
		int result = getNumOfDaysBetweenMonths(month, Month.DECEMBER.get(), year);
		result += monthNumberOfDays(Month.DECEMBER.get(), 2015);
		return result;
	}

	/**
	 *
	 * @param month1
	 * @param month2
	 * @param year
	 * @return
	 */
	public static int getNumOfDaysBetweenMonths(int month1, int month2, int year) {
		int result = 0;
		int numOfMonths = month2 - month1;

		for(int i = 0; i < numOfMonths; i++) {
			result += monthNumberOfDays(month1 + i, year);
		}
		return result;
	}

	/**
	 *
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getNumOfDaysTillEndOfMonth(int day, int month, int year) {
		int numberOfDays = monthNumberOfDays(month, year);
		return numberOfDays - day;
	}

	/**
	 * Returns the number of days in the next month
	 * @return
	 */
	public static int getNumberOfDaysOfNextMonth() {
		Calendar cal = Calendar.getInstance();
		int month = (cal.get(Calendar.MONTH) + 1) % 12;
		int year = (cal.get(Calendar.YEAR) == 1) ? cal.get(Calendar.YEAR) + 1 : cal.get(Calendar.YEAR);
		int numberOfDays = monthNumberOfDays(month, year);
		return numberOfDays;
	}

	/**
	 * Given a year, month, week number, and the number of the day in that week
	 * it returns the date of that day in the month
	 * @param year
	 * @param month
	 * @param weekNumber
	 * @param dayInWeek
	 * @return
	 */
	public static int getDayOfMonth(int year, int month, int weekNumber, int dayInWeek) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH,month-1);
		cal.set(Calendar.WEEK_OF_MONTH,weekNumber);
		cal.set(Calendar.DAY_OF_WEEK,dayInWeek);
		int day = cal.get(Calendar.DATE);
		Date time = cal.getTime();
		return day;
	}

	/**
	 * Given parameter, return the day of week index (0..6)
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH,month-1);
		cal.set(Calendar.DATE,date);
		int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return day;
	}

	/**
	 * Given parameter, return the week index (0..4/5)
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static int getWeekIndex(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH,month);
		cal.set(Calendar.DATE,date);
		Date time = cal.getTime();
		int weekIndex = cal.get(Calendar.WEEK_OF_MONTH) - 1;
		return weekIndex;
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date getDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH,month-1);
		cal.set(Calendar.DATE,day);
		Date time = cal.getTime();
		return time;


	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static boolean isWeekend(int year,int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH,month-1);
		cal.set(Calendar.DATE,day);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int firstDayBeforeWeekend(int year, int month, int day) {
		int result = day;
		boolean isWeekend = isWeekend(year,month,day);
		if(isWeekend) {
			isWeekend = isWeekend(year,month,day-1);
			if(isWeekend) {
				result = day - 2;
			} else {
				result = day - 1;
			}
		}
		return result;
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int firstDayAfterWeekend(int year, int month, int day) {
		int result = day;
		boolean isWeekend = isWeekend(year,month,day);
		if(isWeekend) {
			isWeekend = isWeekend(year,month,day+1);
			if(isWeekend) {
				result = day + 2;
			} else {
				result = day + 1;
			}
		}
		return result;
	}

	/**
	 * Return the current month
	 * @return
	 */
	public static int currentMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 *
	 * @param date
	 * @param interval
	 * @return
	 */
	public static Date getPrevious(TransactionDate date, int interval) {
		int year = date.getYear();
		int month = date.getMonth();
		int day = date.getDay();
		if(day > interval) {
			day = day-interval;
		} else {
			int numberOfDaysInPreviousMonth = monthNumberOfDays(month - 1, year);
			day = interval-day;
			day = numberOfDaysInPreviousMonth - day;
			month--;
		}

		return new Date(year,month,day);
	}

	/**
	 *
	 * @param transactionDate
	 * @param interval
	 * @return
	 */
	public static TransactionDate nextDate(TransactionDate transactionDate, int interval) {
		int numberOfDaysInMonth = monthNumberOfDays(transactionDate.getMonth(), transactionDate.getYear());
		int currentDay = transactionDate.getDay();
		int nextDay = (currentDay + interval) - numberOfDaysInMonth;
		int nextMont = transactionDate.getMonth();
		if(interval > numberOfDaysInMonth - currentDay) {
			nextMont ++;
		}

		Calendar instance = Calendar.getInstance();
		instance.set(transactionDate.getYear(), nextMont - 1, nextDay);
		Date date = instance.getTime();
		return new TransactionDate(date);

	}
}
