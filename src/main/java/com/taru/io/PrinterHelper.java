package com.taru.io;

import com.taru.model.Pair;
import com.taru.model.Transaction;
import com.taru.model.enums.Week;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Shiran Maor on 10/24/2015.
 */
public class PrinterHelper {

  public static String printDetailed(String category,Map<String, Map<String, List<Transaction>>> categoryToMonths, boolean printToScreen) {
    StringBuilder str = new StringBuilder();
    Map<String, List<Transaction>> stringListMap = categoryToMonths.get(category);
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

  public static void printCategoriesNames(Map<String, List<Transaction>> map) {
    Set<String> strings = map.keySet();
    Iterator<String> iterator = strings.iterator();
    while (iterator.hasNext()) {
      String next = iterator.next();
      System.out.println(next);
    }
  }

  public  static void printCategoriesSizes(Map<String, List<Transaction>> map) {
    Set<Map.Entry<String, List<Transaction>>> entries = map.entrySet();
    Iterator<Map.Entry<String, List<Transaction>>> iterator = entries.iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, List<Transaction>> next = iterator.next();
      String key = next.getKey();
      List<Transaction> value = next.getValue();
      System.out.println(key + ": " + value.size());
    }
  }

  public static void printTotals(String categoryToPrint , Map<String, Map<String, List<Transaction>>> categoryToMonths) {
    Set<Map.Entry<String, Map<String, List<Transaction>>>> entries = categoryToMonths.entrySet();
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

  public static void printProjected(List<Pair<Integer, Double>>[] weeklyProjected) {
    for(int i = 0; i < weeklyProjected.length; i++) {
      System.out.println("Printing for week number " + i);
      List<Pair<Integer, Double>> days = weeklyProjected[i];
      if (days != null) {
        Iterator<Pair<Integer, Double>> iterator = days.iterator();
        while(iterator.hasNext()) {
          Pair<Integer, Double> next = iterator.next();
          Week dayWeek = Week.values()[next.getFirst()];
          System.out.println("On day " + dayWeek.toString() + " : " + next.getSecond());
        }
      }
    }
  }

  public static void print(List<Transaction> projectedTransaction) {
    Iterator<Transaction> iterator = projectedTransaction.iterator();
    int i = 1;
    while(iterator.hasNext()) {
      Transaction next = iterator.next();
      System.out.println(i + ".  " + next.getCategory());
      System.out.println("Amount: " + next.getAmount());
      System.out.println("Date: " + next.getTransactionDate().getFullRepresentation());
      System.out.println("-------------------------------");
      i++;
    }
  }
}
