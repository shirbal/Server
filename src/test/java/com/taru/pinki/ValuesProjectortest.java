package com.taru.pinki;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiran Maor on 11/1/2015.
 */
public class ValuesProjectortest extends TestCase {

  @Test
  public void testProjected() throws Exception {
    List<Double> vals = new ArrayList<>();


    vals.add(748.0 );
    vals.add(1464.0);
    vals.add(1720.0);
    vals.add(2047.0);
    vals.add(1923.0);
    vals.add(1123.0);
    vals.add(1420.0);
    vals.add( 1484.0);
    ValuesProjector.removeSDFromList(vals,1.5);
    double projectedForTransaction = ValuesProjector.getProjectedForTransaction(vals, 1.5);
  }

}
