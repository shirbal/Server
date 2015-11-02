package com.taru.pinki;

import com.taru.model.Transaction;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

/**
 * Created by Shiran Maor on 10/31/2015.
 */
public class ProjectorHelperTest extends TestCase {

  @Test
  public void testProjected() throws Exception {
    List<Transaction> projectedTransaction = ProjectorHelper.createProjectedTransaction();
  }


}
