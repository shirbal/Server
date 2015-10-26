package com.taru.utils;

import com.taru.model.TransactionDate;
import com.taru.model.enums.Month;

import java.util.Calendar;

public class DateUtils {

	public static int getNumberOfDays(int month, int year) {
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
	public static int dayBetween(TransactionDate date1, TransactionDate date2) {
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
			result = getNumberOfDays(Month.JANUARY.get(),year);
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
		result += getNumberOfDays(Month.DECEMBER.get(),2015);
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
			result += getNumberOfDays(month1 + i,year);
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
		int numberOfDays = getNumberOfDays(month, year);
		return numberOfDays - day;
	}
}
