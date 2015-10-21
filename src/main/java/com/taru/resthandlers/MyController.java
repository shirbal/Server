package com.taru.resthandlers;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taru.model.Transaction;
import com.taru.resthandlers.services.TransactionService;

@RestController
public class MyController {

	private TransactionService service = TransactionService.getInstance();
    
	@RequestMapping("/months")
	public Map<String, Map<String, List<Transaction>>> getTransactionsByMonths() {
        Map<String, Map<String, List<Transaction>>> map = service.getAllCategoryBytMonths();
        map.remove(null);
        return map;
	}
	
	@RequestMapping("/categ/{catgoryName}")
	public Map<String, List<Transaction>> getByCategor(@PathVariable String categoryName) {
		return service.getCategoryByMonthByCategoryName(categoryName);
	}
	
	@RequestMapping("/catstr/{categoryName}")
	public String getByCategorStr(@PathVariable String categoryName) {
		return service.printDetailed(categoryName,true);
	}
	
}
