package com.taru.pinki;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.taru.model.TransactionDate;
import com.taru.pinki.models.Pair;


public class PinkiCenterTest {

	PinkiCenter service = new PinkiCenter();
	 
	@Test
	public void testCreateAvrageTable() throws Exception {
		List<Pair<TransactionDate,Double>> list = new ArrayList<Pair<TransactionDate, Double>>();
		add(list, 3, 741);
		add(list, 4, 1448);
		add(list, 5, 1706);
		add(list, 6, 2031);
		add(list, 7, 1911);
		add(list, 8, 1113);
		
		service.getProjected(list);
		
	
	}

	private void add(List<Pair<TransactionDate, Double>> list, int month, double amount) {
		@SuppressWarnings("deprecation")
		TransactionDate date = new TransactionDate(new Date(2015,month,1));
		list.add(new Pair<TransactionDate,Double>(date,amount));
	}
}
