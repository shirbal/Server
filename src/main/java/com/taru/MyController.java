package com.taru;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taru.model.Transaction;

@RestController
public class MyController {
    private TransactionReader transactionReader = new TransactionReader();
    private ReaderHelper reader = null;
    
    
    public MyController() {
    	reader = transactionReader.readtFromAccount();	
    }
	
    
	@RequestMapping("/data")
	public MyObject getHello() {
		MyObject obj = new MyObject();
		obj.setId(12);
		obj.setName("Shiran");
		return obj;
	}
	
	@RequestMapping("/trans")
	public Map<String,List<Transaction>> getTransactions() {
        Map<String,List<Transaction>> map = reader.getMap();
        map.remove(null);
        return map;
	}
	
	@RequestMapping("/months")
	public Map<String, Map<String, List<Transaction>>> getTransactionsByMonths() {
        Map<String, Map<String, List<Transaction>>> map = reader.getCategoryBytMonths();
        map.remove(null);
        return map;
	}
	
	@RequestMapping("/categ/{categoryName}")
	public Map<String, List<Transaction>> getByCategor(@PathVariable String categoryName) {
		return reader.getCategoryByMonth(categoryName);
	}
	
	@RequestMapping("/catstr/{categoryName}")
	public String getByCategorStr(@PathVariable String categoryName) {
		return reader.printDetailed(categoryName,false);
	}
	
}
