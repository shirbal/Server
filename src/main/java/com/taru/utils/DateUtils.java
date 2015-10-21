package com.taru.utils;

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
	
}
