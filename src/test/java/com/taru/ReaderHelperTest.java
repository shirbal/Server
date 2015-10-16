package com.taru;

import org.junit.Test;

public class ReaderHelperTest {
    private TransactionReader transactionReader = new TransactionReader();
	@Test
	public void testPrintTotals() throws Exception {
		ReaderHelper rd = transactionReader.readtFromAccount();
		rd.printTotals("Groceries");
	}
	
	@Test
	public void testCategoryDetailed() throws Exception {
		ReaderHelper rd = transactionReader.readtFromAccount();
		rd.printTotals("Groceries");
		//rd.printDetailed("Groceries",true);
	}
	
}
