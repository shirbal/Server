package com.taru.brain;

import java.util.*;

/**
 * Created by Shiran Maor on 10/10/2015.
 */
public class CategoryMapper {
  private static Map<String,String> _known = new HashMap<String, String>();

  static {
    _known.put("DUNKIN", "Food");
    _known.put("MCDONALD'S", "Food");
    _known.put("BURGER KING", "Food");
    _known.put("SCHNITZI", "Food");
    _known.put("PANERA BREAD", "Food");
    _known.put("SBARRO", "Food");
    _known.put("SUBWAY", "Food");
    _known.put("CREAMERY", "Food");
    _known.put("MUSEUM", "Entertainment");
    _known.put("JCC", "Entertainment");
    _known.put("MUSEUM", "Entertainment");
    _known.put("B&N", "Entertainment");
    _known.put("METS", "Entertainment");
    _known.put("SOCCER", "Entertainment");
    _known.put("ART", "Entertainment");
    _known.put("YANKEE", "Entertainment");
    
    
    _known.put("KOSHER", "Food");
    _known.put("YOGURT", "Food");
    _known.put("FOOD", "Food");
    _known.put("VIETNAMESE", "Food");
    _known.put("SPA", "Beauty");
    _known.put("NAIL", "Beauty");
    _known.put("HAIR", "Beauty");
    _known.put("PIZZA", "Food");
    _known.put("PIZZERIA", "Food");
    _known.put("DOMINO'S", "Food");
    _known.put("RESTAURANT", "Food");
    _known.put("BURGER", "Food");
    _known.put("BISTRO", "Food");
    _known.put("DINER", "Food");
    _known.put("BAR & GRILL", "Food");
    _known.put("CAFE", "Food");
    _known.put("COOKING", "Food");
    _known.put("SUSHI", "Food");
    _known.put("ESPRESSO","Food");
    _known.put("BUFFET", "Food");
    _known.put("CHEESECAKE", "Food");
    _known.put("Staples","Miscel");
    _known.put("SHELL", "Transportation");
    _known.put("TOYOTA", "Transportation");
    _known.put("OIL", "Transportation");
    
    
    _known.put("TARGET", "Miscel");
    _known.put("CLEANERS", "Miscel");
    
    _known.put("EZPASS", "Transportation");
    _known.put("VALET", "Transportation");
    _known.put("GAS", "Transportation");
    _known.put("FUEL", "Transportation");
    _known.put("AMOCO", "Transportation");
    _known.put("GCC", "GCC");
    _known.put("AUTO", "Transportation");
    _known.put("LUKOIL", "Transportation");
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
    _known.put("H&M", "Cloths");
    _known.put("SHOES", "Cloths");
    
    _known.put("SPORTS AUTHORI", "Cloths");
    
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
    _known.put("ELECTR", "Electrisity");
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
      //System.out.println("Other: " + in);
      res = "Other";
    }
    return res;
  }
}
