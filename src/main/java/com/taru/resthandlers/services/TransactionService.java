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
import com.taru.model.enums.Bank;
import com.taru.model.Transaction;
import com.taru.model.TransactionCategory;
import com.taru.model.TransactionDate;
import com.taru.parsers.AmountParser;
import com.taru.parsers.DateParser;

public class TransactionService {

	private static TransactionService _instance = new TransactionService();
	private Map<String, Map<String, List<Transaction>>> _categoryToMonths;

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

	public List<Transaction> getTransactionByMonthAsList(String category) {
		List<Transaction> transactions = new LinkedList<>();

		try {
			Map<String, List<Transaction>> transactionsByMonth = _categoryToMonths.get(category);
			if (transactionsByMonth != null) {
        Set<Map.Entry<String, List<Transaction>>> entries = transactionsByMonth.entrySet();
        Iterator<Map.Entry<String, List<Transaction>>> iterator = entries.iterator();
        while(iterator.hasNext()) {
          Map.Entry<String, List<Transaction>> next = iterator.next();
          List<Transaction> value = next.getValue();
          Iterator<Transaction> transactionIterator = value.iterator();
          while(transactionIterator.hasNext()) {
            Transaction next1 = transactionIterator.next();
            transactions.add(next1);
          }
        }
      }
		} catch (Exception e) {
			//LOG this
		}
		return transactions;
	}

	public Map<String, Map<String, List<Transaction>>> getCategoryToMonths() {
		return _categoryToMonths;
	}

	private void init() {
		_categoryToMonths = new HashMap<>();
		List<String[]> res = DataReader.readtFromLeumiAccount("c:\\temp\\transactions.xls");
		//List<String[]> res = DataReader.readtFromBOFAAccount("c:\\temp\\shiranAccount.xls");
		Iterator<String[]> iter = res.iterator();
		Map<String, List<Transaction>> map = new HashMap<>();
		while (iter.hasNext()) {

			String[] element = iter.next();
			if (element != null && element.length == 3) {
				add(element[0], element[1], element[2], map);//,Bank.BOFA);
				//add(element[0], element[1], element[2], map,Bank.BOFA);
			}

		}
		//printCategoriesNames(map);
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
			if (date.getMonth() <=8) {
				addTransactionToList(map, date, parsedAmount, categoryParsed);
			}
		} catch (Exception ex) {
			System.out.println("Exception bummer");
		}
	}

	private void add(String time, String amount, String category, Map<String, List<Transaction>> map, Bank bank) {
		try {
			TransactionDate date = parseDate(time);
			double parsedAmount = parseAmount(amount);
			TransactionCategory categoryParsed = parseCategory(category,bank);
			addTransactionToList(map, date, parsedAmount, categoryParsed);
		} catch (Exception ex) {
			System.out.println("Exception bummer");
		}
	}

	private void addTransactionToList(Map<String, List<Transaction>> map, TransactionDate date, double parsedAmount, TransactionCategory categoryParsed) {
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

	private TransactionCategory parseCategory(String category, Bank bank) {
		return new TransactionCategory(category,bank);
	}



}
