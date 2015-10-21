package com.taru.pinki;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.taru.parsers.DateParser;
import org.junit.Test;

import com.taru.model.TransactionDate;
import com.taru.pinki.models.Pair;


public class PinkiCenterTest {

	PinkiCenter service = new PinkiCenter();
	 
	@Test
	public void testCreateAvrageTable() throws Exception {
		List<Pair<TransactionDate,Double>> list = new ArrayList<Pair<TransactionDate, Double>>();
		add(list, "01-MAR-2015", 741);
		add(list, "01-APR-2015", 1448);
		add(list, "01-MAY-2015", 1706);
		add(list, "01-JUN-2015", 2031);
		add(list, "01-JUL-2015", 1911);
		add(list, "01-AUG-2015", 1113);
		
		service.getProjected(list,2);
		
	
	}

	private void add(List<Pair<TransactionDate, Double>> list, String dateStr, double amount) {
		@SuppressWarnings("deprecation")
		TransactionDate date = DateParser.parseTimestamp(dateStr);
		list.add(new Pair<TransactionDate,Double>(date,amount));
	}
}
