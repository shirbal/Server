package com.taru;

/**
 * Created by Shiran Maor on 10/10/2015.
 */
public class CategoryParser {
	
	private static CategoryParser _instance = new CategoryParser();
	
	private CategoryParser(){}
	
	public static CategoryParser getInstance() {
		return _instance;
	}


  public String parseVisaDebit(String category) {
    category = clearDoubleSpaces(category);
    String[] splits = category.split(" ");
    StringBuilder categ = new StringBuilder();
    for(int i = 4; i < splits.length; i++) {
        categ.append(splits[i]);
        categ.append(" ");
    }
    String res = categ.toString();
    String substring = res.substring(0, res.length() - 1);
    substring = CategoryOrg.getCategory(substring);
    return substring;
  }

  public String parseACHDebit(String category) {
    category = clearDoubleSpaces(category);
    String[] split = category.split(" ");
    StringBuilder categ = new StringBuilder();
    for(int i = 2; i< split.length; i++) {
      if (split[i].charAt(0) != '0') {
        categ.append(split[i]);
        categ.append(" ");
      } else {
        break;
      }

    }
    String res = categ.toString();
    String substring = res.substring(0, res.length() - 1);
    substring = CategoryOrg.getCategory(substring);
    return substring;
  }






  public String clearDoubleSpaces(String str) {
    while(str.contains("  ")) {
      str = str.replace("  "," ");
    }
    return str;
  }

}
