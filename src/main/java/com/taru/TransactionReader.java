package com.taru;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileInputStream;
import java.util.Iterator;

/**
 * Created by Shiran Maor on 10/10/2015.
 */
public class TransactionReader {

  public ReaderHelper readtFromAccount() {
    ReaderHelper reader = new ReaderHelper();
    try {
      FileInputStream file = new FileInputStream("c:\\temp\\transactions.xls");

      //Create Workbook instance holding reference to .xlsx file
      HSSFWorkbook workbook = new HSSFWorkbook(file);

      //Get first/desired sheet from the workbook
      HSSFSheet sheet = workbook.getSheetAt(0);


      //Iterate through each rows one by one
      Iterator<Row> rowIterator = sheet.iterator();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        //For each row, iterate through all the columns
        Cell date = row.getCell(0);
        Cell amount = row.getCell(5);
        if (amount == null) {
          amount = row.getCell(6); // than this amount is income

        }
        Cell category = row.getCell(10);
        String dateStr = date.toString();
        String amountStr = amount.toString();
        String categoryStr = null;
        if (category != null) {
          categoryStr = category.toString();
        }
        if (category != null) {
          reader.add(dateStr, amountStr, categoryStr);
        }
      }
      file.close();
      reader.mapToMonths();

      //System.out.println(reader.numOfCategories());
      //reader.printCategoriesNames();
      //reader.printCategoriesSizes();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reader;
  }

}
