package com.taru.model;

/**
 * Created by Shiran Maor on 10/10/2015.
 */
public class Transaction {

	private TransactionDate _transactionDate;
	private double _amount;
	private TransactionCategory _transactionCategory;

	public Transaction(TransactionDate transasction, double amount, TransactionCategory category) {
		_transactionCategory = category;
		_transactionDate = transasction;
		_amount = amount;
	}

	public TransactionDate getTransactionDate() {
		return _transactionDate;
	}

	public void setTransactionDate(TransactionDate timestamp) {
		_transactionDate = timestamp;
	}

	public double getAmount() {
		return _amount;
	}

	public void setAmount(double amount) {
		_amount = amount;
	}

	public String getCategory() {
		return _transactionCategory.getCategory();
	}

	public String getMonth() {
		return _transactionDate.getDateRepresentation();
	}

}
