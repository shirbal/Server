package com.taru;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crsh.shell.impl.command.system.help;
import org.junit.Test;

import com.taru.model.Transaction;

public class TransactionReaderTest {

	@Test
	public void testReadAll() throws Exception {
		TransactionReader reader = new TransactionReader();
		ReaderHelper rd =  reader.readtFromAccount();
		Map<String, Map<String, List<Transaction>>> map = rd.getCategoryBytMonths();
		Set<String> keys = map.keySet();
		Iterator<String> interator = keys.iterator();
		while(interator.hasNext()) {
			String next = interator.next();
			rd.printTotals(next);
			System.out.println("");
		}
		
	}
	
}
