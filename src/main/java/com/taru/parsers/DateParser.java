package com.taru.parsers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.taru.model.TransactionDate;

public class DateParser {

	private static final String DATE_FORMAT = "dd-MMM-yy";

	public static TransactionDate parseTimestamp(String date) {
		TransactionDate transactionDate = null;
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date timestamp = formatter.parse(date);
			transactionDate = new TransactionDate(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return transactionDate;
	}

}
