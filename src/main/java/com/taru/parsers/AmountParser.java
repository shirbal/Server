package com.taru.parsers;

public class AmountParser {

	public static double parseAmount(String amount) {
		double result = 0;
		try {
			double v = Double.parseDouble(amount);
			result = v;
		} catch (NumberFormatException e) {
			result = -1;
		}
		return result;
	}

}
