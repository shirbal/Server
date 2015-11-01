package com.taru.model.enums;

/**
 * Created by Shiran Maor on 10/25/2015.
 */
public enum Week {
  SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY ;

  public static boolean firstDayOfWeek(int day) {
    return day == SUNDAY.ordinal();
  }

  public static boolean lastDayOfWeek(int day) {
    return day == SATURDAY.ordinal();
  }
}
