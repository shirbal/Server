package com.taru;

import org.junit.Test;

import com.taru.resthandlers.services.TransactionService;

public class ReaderHelperTest {
    private TransactionService service = TransactionService.getInstance();
	@Test
	public void testPrintTotals() throws Exception {
		//service.printTotals("Food");
	}
	
	@Test
	public void testCategoryDetailed() throws Exception {
		//service.printTotals("Groceries");
		//rd.printDetailed("Groceries",true);
	}
	
}
