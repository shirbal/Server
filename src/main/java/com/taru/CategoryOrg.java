package com.taru;

import java.util.*;

/**
 * Created by Shiran Maor on 10/10/2015.
 */
public class CategoryOrg {
  private static Map<String,String> _known = new HashMap<String, String>();

  static {
    _known.put("DUNKIN", "Food");
    _known.put("MCDONALD'S", "Food");
    _known.put("BURGER KING", "Food");
    _known.put("PANERA BREAD", "Food");
    _known.put("SBARRO", "Food");
    _known.put("FOOD", "Food");
    _known.put("PIZZA", "Food");
    _known.put("PIZZERIA", "Food");
    _known.put("DOMINO'S", "Food");
    _known.put("RESTAURANT", "Food");
    _known.put("BURGER", "Food");
    _known.put("BISTRO", "Food");
    _known.put("DINER", "Food");
    _known.put("CAFE", "Food");
    _known.put("COOKING", "Food");
    _known.put("SUSHI", "Food");
    _known.put("BUFFET", "Food");
    _known.put("CHEESECAKE", "Food");
    _known.put("Staples","Miscel");
    _known.put("SHELL", "Transportation");
    _known.put("TARGET", "Miscel");
    _known.put("EZPASS", "Transportation");
    _known.put("GAS", "Transportation");
    _known.put("FUEL", "Transportation");
    _known.put("AMOCO", "Transportation");
    _known.put("GCC", "GCC");
    _known.put("AUTO", "Transportation");
    _known.put("EL AL AIR", "Transportation");
    _known.put("PARKING", "Transportation");
    _known.put("GARAGE", "Transportation");
    _known.put("CAR WASH", "Transportation");
    _known.put("STARBUCKS", "Food");
    _known.put("PORT OF AUTHORITY", "Transportation");
    _known.put("CELLULAR", "Internet & Cellular");
    _known.put("MOBIL", "Internet & Cellular");
    _known.put("PHARMACY", "Pharmacy");
    _known.put("CVS", "Pharmacy");
    _known.put("WALGREENS", "Pharmacy");
    _known.put("SUPERMKTS", "Groceries");
    _known.put("FARMERS", "Groceries");
    _known.put("WHOLEFDS", "Groceries");
    _known.put("Groceries", "Groceries");
    _known.put("MARKET", "Groceries");
    _known.put("MEDIC", "Medical");
    _known.put("HOSPITAL", "Medical");
    _known.put("PEDIATRICS", "Medical");
    _known.put("STATE FARM", "Auto Insurance");
    _known.put("CABLE", "Home");
    _known.put("TECHNICAL", "Home");
    _known.put("VISA PAYMENT", "Credit Card");
    _known.put("VERIZON", "Internet & Cellular");
    _known.put("WATER", "Water");
    _known.put("LOAN", "Loans");
  }

  public static String getCategory(String in) {
    Set<Map.Entry<String, String>> entries = _known.entrySet();
    Iterator<Map.Entry<String, String>> iterator = entries.iterator();
    String res = null;
    while(iterator.hasNext()) {
      Map.Entry<String, String> next = iterator.next();
      String key = next.getKey();
      if(in.contains(key)) {
        res = next.getValue();
        break;
      }
    }

    if(res == null) {
      System.out.println("Other: " + in);
      res = "Other";
    }
    return res;
  }
}
