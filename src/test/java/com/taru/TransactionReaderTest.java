package com.taru;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.taru.model.Transaction;
import com.taru.resthandlers.services.TransactionService;

public class TransactionReaderTest {

	@Test
	public void testReadAll() throws Exception {
		TransactionService service = TransactionService.getInstance();
		Map<String, Map<String, List<Transaction>>> map = service.getAllCategoryBytMonths();
		Set<String> keys = map.keySet();
		Iterator<String> interator = keys.iterator();
		while (interator.hasNext()) {
			String next = interator.next();
			//service.printTotals(next);
			System.out.println("");
		}

	}

}
