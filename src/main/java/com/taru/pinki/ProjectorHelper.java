package com.taru.pinki;

import com.taru.model.*;
import com.taru.resthandlers.services.TransactionService;
import com.taru.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiran Maor on 10/31/2015.
 */
public class ProjectorHelper {

  public static final int MONTH_TO_PROJECT = 9;
  public static final int YEAR_TO_PROJECT = 2015;
  public static final int LAST_MONTH_TO_USE_DATA = 9;
  public static final double SD_TO_USE = 1.5;

  public static List<Transaction> createProjectedTransaction() {
    List<Transaction> result = new ArrayList<>();
    int month = MONTH_TO_PROJECT;
    int year = YEAR_TO_PROJECT;
    updateResultWithExpenses(result, month, year);
    updateResultWithIncome(result, month, year);
    return result;
  }

  private static void updateResultWithIncome(List<Transaction> result, int month, int year) {
    updateResultWithPermanentMultiTransactions(result, "Payroll", TransactionType.IN, month, year);
  }

  private static void updateResultWithExpenses(List<Transaction> result, int month, int year) {
    updateResultWithDistributedTransactions(result, "Groceries", TransactionType.OUT, month, year);
    updateResultWithDistributedTransactions(result, "Food", TransactionType.OUT, month, year);
    updateResultWithPermanentTransactions(result, "Loans", TransactionType.OUT, month, year);
    updateResultWithPermanentTransactions(result, "Electricity", TransactionType.OUT, month, year);
    updateResultWithPermanentTransactions(result, "Auto Insurance", TransactionType.OUT, month, year);
    updateResultWithPermanentTransactions(result, "Cellular", TransactionType.OUT, month, year);
  }

  private static void updateResultWithDistributedTransactions(List<Transaction> result,
                                                              String categoryName,
                                                              TransactionType type,
                                                              int month, int year) {
    int numberOfDaysOfNextMonth = DateUtils.monthNumberOfDays(month, year);
    TransactionService service = TransactionService.getInstance();
    List<Transaction> transactions = service.getTransactionByMonthAsList(categoryName);
    System.out.print("Total montly for " + categoryName);
    List<Pair<Integer, Double>>[] weeklyProjected = PinkiCenter.createWeeklyProjected(transactions, numberOfDaysOfNextMonth, 4, SD_TO_USE);
    updateResultWithWeeklyProjected(result, weeklyProjected, month, year, categoryName, type);
  }

  private static void updateResultWithPermanentTransactions(List<Transaction> result,
                                                            String categoryName,
                                                            TransactionType type,
                                                            int month, int year) {
    TransactionService service = TransactionService.getInstance();
    List<Transaction> transactions = service.getTransactionByMonthAsList(categoryName);
    Pair<Integer, Double> monthlyProjected = PermanentProjector.createMonthlyProjected(transactions, 3.0);
    updateResultWithTransaction(result, month, year, monthlyProjected.getFirst(), categoryName, type, monthlyProjected.getSecond());
  }

  private static void updateResultWithPermanentMultiTransactions(List<Transaction> result,
                                                            String categoryName,
                                                            TransactionType type,
                                                            int month, int year) {
    TransactionService service = TransactionService.getInstance();
    List<Transaction> transactions = service.getTransactionByMonthAsList(categoryName);
    List<Pair<Integer, Double>> monthlyProjected = PermanentProjector.createMonthMultiDaysProjected(transactions, 3.0);
    updateResultWithListOfTransaction(result,monthlyProjected, month, year, categoryName, type);
  }

  private static void updateResultWithWeeklyProjected(List<Transaction> result, List<Pair<Integer, Double>>[] weeklyProjected,
                                                      int month, int year, String categoryName, TransactionType type) {
    int numberOfDays = DateUtils.monthNumberOfDays(month, year);
    for (int i = 1; i <= numberOfDays; i++) {
      int dayOfWeek = DateUtils.getDayOfWeek(year, month, i);
      int weekIndex = DateUtils.getWeekIndex(year, month, i) % 4;
      List<Pair<Integer, Double>> weekTransactions = weeklyProjected[weekIndex];

      for (Pair<Integer, Double> pair : weekTransactions) {
        if (pair.getFirst() == dayOfWeek) {
          updateResultWithTransaction(result, month, year, i, categoryName, type, pair.getSecond());
          break;
        }
      }


    }
  }

  private static void updateResultWithListOfTransaction(List<Transaction> result, List<Pair<Integer,Double>> transactions,
                                                        int month, int year, String categoryName, TransactionType type) {
    for(Pair<Integer,Double> pair: transactions) {
      updateResultWithTransaction(result, month, year, pair.getFirst(), categoryName, type, pair.getSecond());
    }
  }

  private static void updateResultWithTransaction(List<Transaction> result, int month, int year, int day, String categoryName, TransactionType type,
                                                  double amount) {
    boolean isWeekend = DateUtils.isWeekend(year,month,day);
    if(isWeekend) {
      if(type == TransactionType.OUT) {
        int firstDayAfterWeekend = DateUtils.firstDayAfterWeekend(year, month, day);
        int numberOfDays = DateUtils.monthNumberOfDays(month, year);
        if(firstDayAfterWeekend > numberOfDays) {
          return;
        }
        day = firstDayAfterWeekend;
      }
      if(type == TransactionType.IN) {
        day = DateUtils.firstDayBeforeWeekend(year, month, day);
      }
    }
    java.util.Date date = DateUtils.getDate(year, month, day);
    TransactionDate transactionDate = new TransactionDate(date);
    TransactionCategory category = new TransactionCategory(categoryName, type);
    Transaction transaction = new Transaction(transactionDate, amount, category);
    result.add(transaction);
  }


}
