package com.taru.pinki;

import com.taru.model.Pair;
import com.taru.model.Transaction;
import com.taru.model.TransactionDate;
import com.taru.utils.DateUtils;
import com.taru.utils.NumbersUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shiran Maor on 10/31/2015.
 */
public class PermanentProjector {

  public static List<Pair<Integer,Double>> createMonthMultiDaysProjected(List<Transaction> transactions, double sdFactor) {

    List<Pair<Integer,Double>> result = new ArrayList<>();
    List<Transaction> used = new ArrayList<>();
    try {
      int iterator = transactions.size() - 1;
      Transaction transaction = transactions.get(iterator);
      int currentMonth = transaction.getTransactionDate().getMonth() + 1;
      int previousMonth = (currentMonth > 1) ? currentMonth - 1 : 12;
      boolean transactionFromPreviousMonth = isTransactionFromMonth(transaction, previousMonth);
      while(transactionFromPreviousMonth) {
        LinkedList<Transaction> previousTransactions = new LinkedList<>();
        previousTransactions.addFirst(transaction);
        addAllPreviousRelevantTransactionsWithSameDay(transactions, transaction, previousTransactions);
        if (previousTransactions.size() >= 3) {
          used.addAll(previousTransactions);
          Pair<Integer, Double> monthlyProjected = createMonthlyProjected(previousTransactions, sdFactor);
          result.add(monthlyProjected);
        } else {
          int interval = addAllPreviousRelevantTransactionsWithSameInterval(transactions, transaction, previousTransactions);
          if(interval != -1) {
            used.addAll(previousTransactions);
            List<Pair<Integer,Double>> daysProjected = getDaysInInterval(previousTransactions,interval,currentMonth);
            result.addAll(daysProjected);
          }
        }

        iterator--;
        transaction = transactions.get(iterator);
        while(isAlreadyUsed(transaction,previousTransactions)) {
          transactionFromPreviousMonth = isTransactionFromMonth(transaction, previousMonth);
          if(!transactionFromPreviousMonth) {
            break;
          }
          iterator--;
          transaction = transactions.get(iterator);
        }
        transactionFromPreviousMonth = isTransactionFromMonth(transaction, previousMonth);
      }
    } catch (Exception e) {
      //LOG this
    }

    return result;

  }

  private static boolean isAlreadyUsed(Transaction transaction, LinkedList<Transaction> previousTransactions) {
    int indexOf = previousTransactions.indexOf(transaction);
    return indexOf != -1;
  }

  private static List<Pair<Integer, Double>> getDaysInInterval(LinkedList<Transaction> previousTransactions, int interval, int currentMonth) {
    List<Pair<Integer, Double>> result = new ArrayList<>();
    try {
      Transaction lastTransaction = previousTransactions.get(0);
      double amount = createMonthlyAmountProjected(previousTransactions, 3);
      int IntervalCounter = interval;
      boolean keepDoing = true;
      while(keepDoing) {
        TransactionDate nextDate = DateUtils.nextDate(lastTransaction.getTransactionDate(), IntervalCounter);
        if(nextDate.getMonth() == currentMonth) {
          int day = nextDate.getDay();
          if(DateUtils.isWeekend(nextDate.getYear(),nextDate.getMonth(),nextDate.getDay())) {
            day = DateUtils.firstDayBeforeWeekend(nextDate.getYear(), nextDate.getMonth(), nextDate.getDay());
          }
          result.add(new Pair<>(day,amount));
        } else {
          break;
        }
        IntervalCounter += interval;
      }
    } catch (Exception e) {
      // LOG this
    }

    return result;
  }

  private static int addAllPreviousRelevantTransactionsWithSameInterval(List<Transaction> transactions, Transaction transaction,
                                                                         LinkedList<Transaction> previousTransactions) {
    int intervalResult = -1;
    int currentMonth = transaction.getTransactionDate().getMonth();
    for(int i = transactions.size() - 1; i >=0 ; i--) {
      Transaction current = transactions.get(i);
      if(current.getTransactionDate().getMonth() == currentMonth) {
        if(sameAmount(current,transaction) && !sameDate(current, transaction)) {
          int interval = DateUtils.daysBetween(current.getTransactionDate(),transaction.getTransactionDate());
          LinkedList<Transaction> prev = new LinkedList<>();
          previousTransactions.addFirst(transaction);
          addAllPreviousRelevantTransactionsWithSameInterval(transactions,transaction,prev,interval);
          if(prev.size() >= 3) {
            previousTransactions.addAll(prev);
            intervalResult = interval;
            break;
          }
        }
      }
      if(current.getTransactionDate().getMonth() < currentMonth) {
        break;
      }
    }
    return intervalResult;
  }

  private static boolean sameDate(Transaction current, Transaction transaction) {
    TransactionDate currentTransactionDate = current.getTransactionDate();
    TransactionDate transactionDate = transaction.getTransactionDate();
    return currentTransactionDate.getYear() == transactionDate.getYear() &&
        currentTransactionDate.getMonth() == transactionDate.getMonth() &&
        currentTransactionDate.getDay() == transactionDate.getDay();
  }

  private static void addAllPreviousRelevantTransactionsWithSameInterval(List<Transaction> transactions, Transaction transaction, LinkedList<Transaction> previousTransactions, int interval) {
    Transaction last = transaction;
    for(int i = transactions.size() - 1; i >=0 ; i--) {
      Transaction current = transactions.get(i);
      if(sameAmount(current,transaction)) {
        int currentInterval = DateUtils.daysBetween(current.getTransactionDate(),last.getTransactionDate());
        if(currentInterval >= interval - 2 && currentInterval <= interval + 2) {
          previousTransactions.add(current);
          last = current;
        }
      }
    }
  }

  private static void addAllPreviousRelevantTransactionsWithSameDay(List<Transaction> transactions, Transaction transaction,
                                                                    LinkedList<Transaction> previousTransactions) {

    int counter = 1;
    for(int i = transactions.size() - 1; i >=0 ; i--) {
      Transaction current = transactions.get(i);
      int currentMonth = current.getTransactionDate().getMonth();
      int startMonth = transaction.getTransactionDate().getMonth();
      if(((startMonth - currentMonth) == counter) && sameDayDifferentMonth(current,transaction) && sameAmount(current,transaction)) {
        previousTransactions.add(current);
        counter++;
      }
    }
  }

  private static boolean sameDayDifferentMonth(Transaction current, Transaction transaction) {
    int currentDay = current.getTransactionDate().getDay();
    int day = transaction.getTransactionDate().getDay();

    boolean isSameDay = currentDay == day;
    boolean isDifferentButWeenend = false;

    boolean isCurrentWeekend = DateUtils.isWeekend(current.getTransactionDate().getYear(),current.getTransactionDate().getMonth(),day);
    if(isCurrentWeekend) {
      int firstDayBeforeWeekend = DateUtils.firstDayBeforeWeekend(current.getTransactionDate().getYear(), current.getTransactionDate().getMonth(), day);
      isDifferentButWeenend =   firstDayBeforeWeekend == currentDay;
    }



    boolean isSamedayPlusMinusOne = (currentDay + 2 == day) || (currentDay - 2 == day) ;
    return isSameDay || isDifferentButWeenend || isSamedayPlusMinusOne;

  }

  private static boolean sameAmount(Transaction current, Transaction transaction) {
    double amount = transaction.getAmount();
    double upperLevelAmount = upperLevel(amount);
    double lowerLevelAmount = lowerLevel(amount);
    return current.getAmount() >= lowerLevelAmount && current.getAmount() <= upperLevelAmount;

  }

  private static double lowerLevel(double amount) {
    double result = amount * 0.9;
    result = NumbersUtils.round(result,2);
    return result;
  }

  private static double upperLevel(double amount) {
    double result = amount + amount * 0.1;
    result = NumbersUtils.round(result,2);
    return result;
  }

  /**
   * Checks if this transaction month as the previous month
   * @param transaction
   * @param previouseMonth
   * @return
   */
  private static boolean isTransactionFromMonth(Transaction transaction, int previouseMonth) {
    boolean result = false;
    int month = transaction.getTransactionDate().getMonth();
    result = month == previouseMonth;
    return result;
  }

  public static Pair<Integer,Double> createMonthlyProjected(List<Transaction> transactions, double sdFactor) {
    Pair<Integer,Double> result = new Pair<>();

    try {

      // 1. calculate projected amount for this payment
      double projectedForTransaction = createMonthlyAmountProjected(transactions, sdFactor);
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

  private static double createMonthlyAmountProjected(List<Transaction> transactions, double sdFactor) {
    // 0. Create a list of amounts only
    List<Double> values = fromTransactionToAmounts(transactions);
    // 1. calculate projected amount for this payment
    return ValuesProjector.getProjectedForTransaction(values, sdFactor);
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
