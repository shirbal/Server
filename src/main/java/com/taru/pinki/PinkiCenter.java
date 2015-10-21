package com.taru.pinki;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.taru.model.TransactionDate;
import com.taru.pinki.models.Pair;
import com.taru.utils.DateUtils;
import com.taru.utils.NumbersUtils;

public class PinkiCenter {

	public double getProjected(List<Pair<TransactionDate,Double>> values, int cleaningParam) {
		double res = 0;
		// Convert monthly total to per day each month amount
		List<Double> perDay = createListPerMonthlyDays(values);
		// Clean list from irregular values
		removeSDFromList(perDay,cleaningParam);
		// get the forecast value
		res = getForcastValue(perDay);
		return res;
	}

	public double getForcastValue(List<Double> perDay) {
		double res;
		// create table for projection
		double[][] table = createAvrageTable(perDay);
		// Calculate projected value
		//res = getMinMad(table);
		res = getAvaregeUsingWeigth(table);
		return res;
	}

	private double getAvaregeUsingWeigth(double[][] table) {
		return table[table.length-1][2];
	}


	private List<Double> createListPerMonthlyDays(List<Pair<TransactionDate,Double>> values) {
		List<Double> perDay = new LinkedList<>();
		Iterator<Pair<TransactionDate,Double>> iter = values.iterator();
		while (iter.hasNext()) {
			Pair<TransactionDate,Double> pair = iter.next();
			int month = pair.getFirst().getMonth();
			int year = pair.getFirst().getYear();
			int daysPerMonth = DateUtils.getNumberOfDays(month, year);
			double amountPerDay = pair.getSecond()/daysPerMonth;
			amountPerDay = NumbersUtils.round(amountPerDay, 2);
			perDay.add(amountPerDay);

		}

		return perDay;

	}

	private void removeSDFromList(List<Double> values, int count) {
		double mean = NumbersUtils.mean(values);
		double sd = NumbersUtils.SD(values, mean);
		sd = NumbersUtils.round(sd,1);
		Iterator<Double> iter = values.iterator();
		while (iter.hasNext()) {
			Double val = iter.next();

			if ((val > (mean + count * sd)) || (val < (mean - count * sd))) {
				iter.remove();
			}
		}
	}

	private double[][] createAvrageTable(List<Double> months) {
		if (months == null || months.size() < 4) {
			return null;
		}

		double res[][] = new double[months.size() + 1][4];

		// Fill basic
		for (int i = 0; i < months.size(); i++) {
			res[i][0] = months.get(i);
		}

		for (int i = 3; i < months.size() + 1; i++) {
			res[i][1] = (res[i - 1][0] + res[i - 2][0] + res[i - 3][0]) / 3;
			res[i][2] = (0.2 * res[i - 3][0]) + (0.3 * res[i - 2][0]) + (0.5 * res[i - 1][0]);
		}

		return res;
	}

	private double getMadOfAverage(double[][] arr) {
		return getMad(arr, 1);
	}

	private double getMadOfWeigthAverage(double[][] arr) {
		return getMad(arr, 2);
	}

	private double getMad(double[][] arr, int index) {

		double sumDeviation = 0;
		int j = 3;
		for (int i = 0; i < 3; i++) {
			double deviation = arr[j][0] - arr[j][index];
			deviation = NumbersUtils.round(deviation, 2);
			double absDeviation = Math.abs(deviation);
			sumDeviation += absDeviation;
			j++;
		}
		return sumDeviation / 3;

	}

	private double getMinMad(double[][] table) {
		double averageMad = getMadOfAverage(table);
		double wiegthAverage = getMadOfWeigthAverage(table);
		return Math.min(averageMad, wiegthAverage);
	}

}
