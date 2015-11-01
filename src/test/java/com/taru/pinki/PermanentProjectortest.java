package com.taru.pinki;

import com.taru.model.Pair;
import com.taru.model.Transaction;
import com.taru.resthandlers.services.TransactionService;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

/**
 * Created by Shiran Maor on 10/31/2015.
 */
public class PermanentProjectortest extends TestCase {
  TransactionService service = TransactionService.getInstance();

  @Test
     public void testCreateMonthlyProjected() throws Exception {
    List<Transaction> cellular = service.getTransactionByMonthAsList("Cellular");
    Pair<Integer, Double> monthlyProjected = PermanentProjector.createMonthlyProjected(cellular, 3);
    Integer res = monthlyProjected.getFirst();
    assertEquals(6, res.intValue());
  }

  @Test
  public void testCreateMonthlyProjected1() throws Exception {
    List<Transaction> cellular = service.getTransactionByMonthAsList("Auto Insurance");
    Pair<Integer, Double> monthlyProjected = PermanentProjector.createMonthlyProjected(cellular, 3);
    Integer res = monthlyProjected.getFirst();
    assertEquals(17, res.intValue());
  }

}
