package com.taru.resthandlers.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.taru.io.DataReader;
import com.taru.model.Transaction;
import com.taru.model.TransactionCategory;
import com.taru.model.TransactionDate;
import com.taru.parsers.AmountParser;
import com.taru.parsers.DateParser;

public class TransactionService {

	private Map<String, Map<String, List<Transaction>>> _categoryToMonths;

	private static TransactionService _instance = new TransactionService();

	private TransactionService() {
		init();
	}

	public static TransactionService getInstance() {
		return _instance;
	}

	public Map<String, Map<String, List<Transaction>>> getAllCategoryBytMonths() {
		return _categoryToMonths;
	}

	public Map<String, List<Transaction>> getCategoryByMonthByCategoryName(String category) {
		return _categoryToMonths.get(category);
	}

	public String printDetailed(String category, boolean printToScreen) {
		StringBuilder str = new StringBuilder();
		Map<String, List<Transaction>> stringListMap = _categoryToMonths.get(category);
		Set<Map.Entry<String, List<Transaction>>> entries = stringListMap.entrySet();
		Iterator<Map.Entry<String, List<Transaction>>> iterator = entries.iterator();

		if (printToScreen) {
			System.out.println("Printing detailed expenses for: " + category);
		}

		str.append("Printing detailed expenses for: " + category);

		while (iterator.hasNext()) {
			Map.Entry<String, List<Transaction>> next = iterator.next();
			String month = next.getKey();
			List<Transaction> transactions = next.getValue();
			if (printToScreen) {
				System.out.println("--------------------------------------------");
				System.out.println("On month: " + month);
			}
			str.append("--------------------------------------------");
			str.append("On month: " + month);
			Iterator<Transaction> iterator1 = transactions.iterator();
			while (iterator1.hasNext()) {
				Transaction next1 = iterator1.next();
				if (printToScreen) {
					System.out.println(next1.getTransactionDate().getDebug() + ", " + next1.getAmount());
				}
				str.append(next1.getTransactionDate().getDebug() + ", " + next1.getAmount());
			}

		}
		return str.toString();
	}

	public void printCategoriesNames(Map<String, List<Transaction>> map) {
		Set<String> strings = map.keySet();
		Iterator<String> iterator = strings.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			System.out.println(next);
		}
	}

	public void printCategoriesSizes(Map<String, List<Transaction>> map) {
		Set<Map.Entry<String, List<Transaction>>> entries = map.entrySet();
		Iterator<Map.Entry<String, List<Transaction>>> iterator = entries.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<Transaction>> next = iterator.next();
			String key = next.getKey();
			List<Transaction> value = next.getValue();
			System.out.println(key + ": " + value.size());
		}
	}

	public void printTotals(String categoryToPrint) {
		Set<Map.Entry<String, Map<String, List<Transaction>>>> entries = _categoryToMonths.entrySet();
		Iterator<Map.Entry<String, Map<String, List<Transaction>>>> iterator = entries.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Map<String, List<Transaction>>> next = iterator.next();
			String categoryName = next.getKey();
			if (!categoryName.equals(categoryToPrint)) {
				continue;
			}
			System.out.println();
			System.out.println("Showing totla for: " + categoryName);
			System.out.println("---------------------------------------");
			Map<String, List<Transaction>> value = next.getValue();
			Set<Map.Entry<String, List<Transaction>>> entries1 = value.entrySet();
			Iterator<Map.Entry<String, List<Transaction>>> iterator1 = entries1.iterator();
			while (iterator1.hasNext()) {
				Map.Entry<String, List<Transaction>> next1 = iterator1.next();
				String key = next1.getKey();

				double total = 0;
				List<Transaction> value1 = next1.getValue();
				Iterator<Transaction> iterator2 = value1.iterator();
				while (iterator2.hasNext()) {
					Transaction next2 = iterator2.next();
					total += next2.getAmount();
				}
				total = Math.round(total);
				System.out.println("The total for Date: " + key + " is: " + total + " in category: " + categoryName);

			}
		}
	}

	private void init() {
		_categoryToMonths = new HashMap<>();
		List<String[]> res = DataReader.readtFromAccount("c:\\temp\\transactions.xls");
		Iterator<String[]> iter = res.iterator();
		Map<String, List<Transaction>> map = new HashMap<>();
		while (iter.hasNext()) {

			String[] element = iter.next();
			if (element != null && element.length == 3) {
				add(element[0], element[1], element[2], map);
			}

		}
		mapToMonths(map);
	}

	private void mapToMonths(Map<String, List<Transaction>> map) {
		_categoryToMonths = new HashMap<>();
		Set<Map.Entry<String, List<Transaction>>> entries = map.entrySet();
		Iterator<Map.Entry<String, List<Transaction>>> iterator = entries.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<Transaction>> next = iterator.next();
			String categoryName = next.getKey();
			if (categoryName == null) {
				continue;
			}
			List<Transaction> transactions = next.getValue();
			Map<String, List<Transaction>> monthsToTransactions = _categoryToMonths.get(categoryName);
			if (monthsToTransactions == null) {
				monthsToTransactions = new TreeMap<>();
				_categoryToMonths.put(categoryName, monthsToTransactions);
			}

			Iterator<Transaction> transactionIterator = transactions.iterator();
			while (transactionIterator.hasNext()) {
				Transaction transaction = transactionIterator.next();
				String current = transaction.getMonth();
				if (current == null) {
					continue;
				}
				List<Transaction> transactionsToMonth = monthsToTransactions.get(current);
				if (transactionsToMonth == null) {
					transactionsToMonth = new ArrayList<>();
					monthsToTransactions.put(current, transactionsToMonth);
				}
				transactionsToMonth.add(transaction);
			}
		}

	}

	private void add(String time, String amount, String category, Map<String, List<Transaction>> map) {
		try {
			TransactionDate date = parseDate(time);
			double parsedAmount = parseAmount(amount);
			TransactionCategory categoryParsed = parseCategory(category);
			if (categoryParsed.getCategory() != null) {
				Transaction transaction = new Transaction(date, parsedAmount, categoryParsed);
				String parsedCategory = transaction.getCategory();
				List<Transaction> list = map.get(parsedCategory);
				if (list == null) {
					list = new LinkedList<>();
					map.put(parsedCategory, list);
				}
				list.add(transaction);
			}

		} catch (Exception ex) {
			System.out.println("Exception bummer");
		}

	}

	private TransactionDate parseDate(String date) {
		return DateParser.parseTimestamp(date);
	}

	private double parseAmount(String amount) {
		return AmountParser.parseAmount(amount);
	}

	private TransactionCategory parseCategory(String category) {
		return new TransactionCategory(category);
	}

}
