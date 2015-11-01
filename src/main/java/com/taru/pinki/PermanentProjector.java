package com.taru.pinki;

import com.taru.model.Pair;
import com.taru.model.Transaction;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shiran Maor on 10/31/2015.
 */
public class PermanentProjector {

  public static Pair<Integer,Double> createMonthlyProjected(List<Transaction> transactions, double sdFactor) {
    Pair<Integer,Double> result = new Pair<>();

    try {
      // 0. Create a list of amounts only
      List<Double> values = fromTransactionToAmounts(transactions);
      // 1. calculate projected amount for this payment
      double projectedForTransaction = ValuesProjector.getProjectedForTransaction(values, sdFactor);
      result.setSecond(projectedForTransaction);
      // 2. calculate date for this payment
      List<Double> days = fromTransactionsToDays(transactions);
      ValuesProjector.removeSDFromList(days,sdFactor);
      int projectedDay = DaysProjector.getProjectedDay(days);
      result.setFirst(projectedDay);
    } catch (Exception e) {
      //LOG this
    }

    return result;
  }

  /**
   * Convert a list of transactions to list of amounts only
   * @param transactions
   *    List of transaction to convert
   * @return
   *    List of amounts (type Double)
   */
  public static List<Double> fromTransactionToAmounts(List<Transaction> transactions) {
    List<Double> result = new LinkedList<>();
    try {
      if(transactions != null) {
        transactions.forEach((transaction) ->  result.add(transaction.getAmount()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Convert a list of transactions to list of dates only
   * @param transactions
   *    List of transaction to convert
   * @return
   *    List of dates (type TransactionDate)
   */
  private static List<Double> fromTransactionsToDays(List<Transaction> transactions) {
    List<Double> result = new LinkedList<>();
    try {
      if(transactions != null) {
        transactions.forEach((transaction) -> result.add((double)transaction.getTransactionDate().getDay()));
      }
    } catch (Exception e) {
      //LOG this
    }
    return result;
  }

}
