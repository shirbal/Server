package com.taru.model;


import com.taru.CategoryParser;
import com.taru.TransactionDate;

import java.util.*;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Shiran Maor on 10/10/2015.
 */
public class Transaction {

  public static final String DATE_FORMAT = "dd-MMM-yy";
  private TransactionDate _transactionDate;
  private double _amount;
  private String _category;
  private TransactionType _transactionType;

  public Transaction(String timestamp, String amount, String category) {
    parseTimestamp(timestamp);
    parseAmount(amount);
    parseCategory(category);
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
    } else if(category.contains("MONEY TRANSFER")) {
    	_transactionType = TransactionType.OUT;
      _category = "Money Transfer";
    } else if(category.contains("SERVICE CHARGE")) {
    	_transactionType = TransactionType.OUT;
      _category = "Service Charge";
    } else if(category.contains("TRANSFER OF FUNDS") || category.contains("REGULAR DEPOSIT")) {
    	_transactionType = TransactionType.IN;
      _category = "Income Funds";
    }

    else {
     //System.out.println(category);
    }
  }

  private void parseDebit(String category) {
    _category = CategoryParser.getInstance().parseVisaDebit(category);
  }

  private void parseACHDebit(String category) {
    _category = CategoryParser.getInstance().parseACHDebit(category);
  }


  private void parseAmount(String amount) {
    try {
      double v = Double.parseDouble(amount);
      _amount = v;
    } catch (NumberFormatException e) {
      _amount = -1;
    }
  }

  private void parseTimestamp(String date) {
    DateFormat formatter =new SimpleDateFormat(DATE_FORMAT);
     try{
      Date timestamp = formatter.parse(date);
      _transactionDate = new TransactionDate(timestamp);
    } catch (ParseException e) {
      e.printStackTrace();
    }
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
    return _category;
  }

  public void setCategory(String category) {
    _category = category;
  }

  public TransactionType getType() {
    return _transactionType;
  }

  public void setType(TransactionType type) {
	  _transactionType = type;
  }

  public String getMonth() {
	  return _transactionDate.getDateRepresentation();
  }
  
}
