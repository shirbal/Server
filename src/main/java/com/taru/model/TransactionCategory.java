package com.taru.model;

import com.taru.parsers.CategoryParser;

public class TransactionCategory {

	private String _category;
	private TransactionType _transactionType;

	public TransactionCategory(String categoryName) {
		parseCategory(categoryName);
	}

	private void parseCategory(String category) {
		if (category.contains("VISA DEBIT")) {
			_transactionType = TransactionType.OUT;
			parseDebit(category);
		} else if (category.contains("ACH DEBIT")) {
			_transactionType = TransactionType.OUT;
			parseACHDebit(category);
		} else if (category.contains("IN-STORE")) {
			_transactionType = TransactionType.OUT;
			_category = "Groceries";
		} else if (category.contains("CUSTOMER CHECK")) {
			_transactionType = TransactionType.OUT;
			_category = "Customer Check";
		} else if (category.contains("ATM")) {
			_transactionType = TransactionType.OUT;
			_category = "ATM";
		} else if (category.contains("ACH CREDIT")) {
			_transactionType = TransactionType.IN;
			_category = "Payroll";
		} else if (category.contains("MONEY TRANSFER")) {
			_transactionType = TransactionType.OUT;
			_category = "Money Transfer";
		} else if (category.contains("SERVICE CHARGE")) {
			_transactionType = TransactionType.OUT;
			_category = "Service Charge";
		} else if (category.contains("TRANSFER OF FUNDS") || category.contains("REGULAR DEPOSIT")) {
			_transactionType = TransactionType.IN;
			_category = "Income Funds";
		}

		else {
			// System.out.println(category);
		}
	}

	private void parseDebit(String category) {
		_category = CategoryParser.getInstance().parseVisaDebit(category);
	}

	private void parseACHDebit(String category) {
		_category = CategoryParser.getInstance().parseACHDebit(category);
	}

	/**
	 * @return the _category
	 */
	public String getCategory() {
		return _category;
	}

	/**
	 * @param _category
	 *            the _category to set
	 */
	public void setCategory(String category) {
		this._category = category;
	}

	/**
	 * @return the _transactionType
	 */
	public TransactionType getTransactionType() {
		return _transactionType;
	}

	/**
	 * @param transactionType
	 *            the _transactionType to set
	 */
	public void setTransactionType(TransactionType transactionType) {
		this._transactionType = transactionType;
	}

}
