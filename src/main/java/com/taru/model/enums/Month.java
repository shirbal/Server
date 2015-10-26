package com.taru.model.enums;

/**
 * Created by Shiran Maor on 10/25/2015.
 */
public enum Month {

  JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4),MAY(5), JUNE(6),
  JULY(7), AUGUST(8),SEPTEMBER(9), OCTOBER(10), NOVEMBER(11), DECEMBER(12) ;

  private int code;

  private Month(int code) {
    this.code = code;
  }

  public int get() {
    return code;
  }


}
