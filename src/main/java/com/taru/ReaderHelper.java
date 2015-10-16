package com.taru;

import com.taru.model.Transaction;

import java.util.*;

/**
 * Created by Shiran Maor on 10/10/2015.
 */
public class ReaderHelper {

  private Map<String,List<Transaction>> _map = new HashMap<String, List<Transaction>>();
  private Map<String, Map<String, List<Transaction>>> _categoryToMonths;

  public Map<String,List<Transaction>> getMap() {
	  return _map;
  }
  
  public Map<String, Map<String, List<Transaction>>> getCategoryBytMonths() {
	  return _categoryToMonths;
  }
  
  public void printTotals(String categoryToPrint) {
    Set<Map.Entry<String, Map<String, List<Transaction>>>> entries = _categoryToMonths.entrySet();
    Iterator<Map.Entry<String, Map<String, List<Transaction>>>> iterator = entries.iterator();
    while(iterator.hasNext()) {
      Map.Entry<String, Map<String, List<Transaction>>> next = iterator.next();
      String categoryName = next.getKey();
      if(!categoryName.equals(categoryToPrint)) {
    	  continue;
      }
      System.out.println();
      System.out.println("Showing totla for: " + categoryName);
      System.out.println("---------------------------------------");
      Map<String, List<Transaction>> value = next.getValue();
      Set<Map.Entry<String, List<Transaction>>> entries1 = value.entrySet();
      Iterator<Map.Entry<String, List<Transaction>>> iterator1 = entries1.iterator();
      while(iterator1.hasNext()) {
        Map.Entry<String, List<Transaction>> next1 = iterator1.next();
        String key = next1.getKey();

        double total = 0;
        List<Transaction> value1 = next1.getValue();
        Iterator<Transaction> iterator2 = value1.iterator();
        while(iterator2.hasNext()) {
          Transaction next2 = iterator2.next();
          total+= next2.getAmount();
        }
        total = Math.round(total);
        System.out.println("The total for Date: " + key + " is: " + total + " in category: " + categoryName);

      }
    }
  }

  /**
   * Return list of transaction for a given category
   * @param category
   * @return
   */
  public Map<String, List<Transaction>> getCategoryByMonth(String category) {
	  return _categoryToMonths.get(category);
  }

  
  
  public String printDetailed(String category, boolean printToScreen) {
    StringBuilder str = new StringBuilder();
	  Map<String, List<Transaction>> stringListMap = _categoryToMonths.get(category);
    Set<Map.Entry<String, List<Transaction>>> entries = stringListMap.entrySet();
    Iterator<Map.Entry<String, List<Transaction>>> iterator = entries.iterator();

    if(printToScreen) {
    	System.out.println("Printing detailed expenses for: " + category);
    }
    
    str.append("Printing detailed expenses for: " + category);
    str.append("\n");
    while(iterator.hasNext()) {
      Map.Entry<String, List<Transaction>> next = iterator.next();
      String month = next.getKey();
      List<Transaction> transactions = next.getValue();
     if(printToScreen) {
    	System.out.println("--------------------------------------------");
      	System.out.println("On month: " + month + "\n");
     }
      str.append("--------------------------------------------" + "<br\\>");
      str.append("On month: " + month + "<br\\>");
      Iterator<Transaction> iterator1 = transactions.iterator();
      while(iterator1.hasNext()) {
        Transaction next1 = iterator1.next();
        if(printToScreen) {
        	System.out.println(next1.getTransactionDate() + ", " + next1.getAmount());
        }
        str.append(next1.getTransactionDate() + ", " + next1.getAmount()+ "<br\\>");
      }

    }
    return str.toString();
  }

  public void mapToMonths() {
    _categoryToMonths = new HashMap<String, Map<String, List<Transaction>>>();
    Set<Map.Entry<String, List<Transaction>>> entries = _map.entrySet();
    Iterator<Map.Entry<String, List<Transaction>>> iterator = entries.iterator();
    while(iterator.hasNext()) {
      Map.Entry<String, List<Transaction>> next = iterator.next();
      String categoryName = next.getKey();
      if(categoryName == null) {
        continue;
      }
      List<Transaction> transactions = next.getValue();
      Map<String, List<Transaction>> monthsToTransactions = _categoryToMonths.get(categoryName);
      if(monthsToTransactions == null) {
        monthsToTransactions = new TreeMap<String, List<Transaction>>();
        _categoryToMonths.put(categoryName, monthsToTransactions);
      }


      Iterator<Transaction> transactionIterator = transactions.iterator();
      while(transactionIterator.hasNext()) {
        Transaction transaction = transactionIterator.next();
        String current = transaction.getMonth();
        if(current == null) {
        	continue;
        }
        List<Transaction> transactionsToMonth = monthsToTransactions.get(current);
        if(transactionsToMonth == null) {
          transactionsToMonth = new ArrayList<Transaction>();
          monthsToTransactions.put(current,transactionsToMonth);
        }
        transactionsToMonth.add(transaction);
      }
    }


  }

  public void add(String time, String amount, String category) {
    Transaction transaction = new Transaction(time,amount,category);
    String parsedCategory = transaction.getCategory();
    List<Transaction> list = _map.get(parsedCategory);
    if(list == null) {
      list = new LinkedList<Transaction>();
      _map.put(parsedCategory,list);
    }
    list.add(transaction);
  }

  public int numOfCategories() {
    return _map.size();
  }

  public void printCategoriesNames() {
    Set<String> strings = _map.keySet();
    Iterator<String> iterator = strings.iterator();
    while(iterator.hasNext()) {
      String next = iterator.next();
      System.out.println(next);
    }
  }

  public void printCategoriesSizes() {
    Set<Map.Entry<String, List<Transaction>>> entries = _map.entrySet();
    Iterator<Map.Entry<String, List<Transaction>>> iterator = entries.iterator();
    while(iterator.hasNext()) {
      Map.Entry<String, List<Transaction>> next = iterator.next();
      String key = next.getKey();
      List<Transaction> value = next.getValue();
      System.out.println(key + ": " + value.size());
    }
  }


}
