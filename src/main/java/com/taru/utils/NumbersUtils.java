package com.taru.utils;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import com.taru.model.Transaction;

public class NumbersUtils {

	

	public static double rount(double num, int digits) {
		StringBuilder str = new StringBuilder("#");
		if(digits > 0) {
			str.append(".");
		}
		while(digits > 0) {
			str.append("#");
			digits--;
		}
		DecimalFormat df = new DecimalFormat(str.toString());
		double res = num;
		try {
			res = Double.valueOf(df.format(num));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;

	}
	
	public static double mean(List<Integer> values) {
		double mean = 0;
		Iterator<Integer> iter = values.iterator();
		while(iter.hasNext()) {
			Integer next = iter.next();
			mean+=next;
			
		}
		mean = mean / values.size();
		return mean;
	}
	
	public static double SD(List<Integer> values, double mean) {
		double res = 0;
		int sum = 0;
		for(int i = 0; i<values.size(); i++) {
			sum += Math.pow((values.get(i) - mean), 2);
		}
		sum /= values.size();
		res = Math.sqrt(sum);
		return res;
	}

}
