package com.taru.model;

import com.taru.model.enums.Week;

import java.util.Comparator;
import java.util.Date;

public class TransactionDate implements Comparator<TransactionDate> {

  private String _date;
  private String debug;

  @SuppressWarnings("deprecation")
  public TransactionDate(Date date) {
    _date = this.getDateMonth(date);
    debug = fromIntToDayString( date.getDay());
  }

  private String fromIntToDayString(int day) {
    Week[] vals = Week.values();
    return vals[day].toString();
  }

	public String getDebug() {
		int month = getMonth();
		int year = getYear();
		int day = getDay();
		return month + " / " + "(" + debug + ") " + day + " / " + year;
	}

  public String getDate() {
    return _date;
  }

  public void setDate(String date) {
    this._date = date;
  }

  public int getYear() {
    String[] date = _date.split("/");
    return Integer.parseInt(date[2]);
  }

  public int getMonth() {
    String[] date = _date.split("/");
    return Integer.parseInt(date[0]);
  }

  public int getDay() {
    String[] date = _date.split("/");
    return Integer.parseInt(date[1]);
  }

  public String getDateRepresentation() {
    String[] date = _date.split("/");
    return date[0] + "/" + date[2];
  }

  public String getFullRepresentation() {
    String[] date = _date.split("/");
    return date[0] + "/" + date[1] + "/" + date[2];
  }

  @SuppressWarnings("deprecation")
  private String getDateMonth(Date timestamp) {
    int month = timestamp.getMonth() + 1;
    int year = timestamp.getYear() + 1900;
    int day = timestamp.getDate();
    return month + "/" + day + "/" + year;
  }

  @Override
  public int compare(TransactionDate o1, TransactionDate o2) {
    int res = 0;

    int o1Year = o1.getYear();
    int o2Year = o2.getYear();
    if (o1Year == o2Year) {
      int o1Month = o1.getMonth();
      int o2Month = o2.getMonth();
      if (o1Month < o2Month) {
        res = -1;
      } else if (o1Month > o2Month) {
        res = 1;
      } else {
        res = 0;
      }
    } else if (o1Year < o2Year) {
      res = -1;
    } else {
      res = 1;
    }
    return res;
  }

  @Override
  public String toString() {
    return _date;
  }

}
