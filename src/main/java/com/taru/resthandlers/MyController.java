package com.taru.resthandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.taru.io.PrinterHelper;
import com.taru.model.Pair;
import com.taru.pinki.PinkiCenter;
import com.taru.pinki.ProjectorHelper;
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

	@RequestMapping("/months/{categoryName}")
	public Map<String, Map<String, List<Transaction>>> getTotalMonths(@PathVariable String categoryName) {
		Map<String, Map<String, List<Transaction>>> map = service.getAllCategoryBytMonths();
		PrinterHelper.printTotals(categoryName,map);
		return map;
	}
	
	@RequestMapping("/categ/{categoryName}")
	public Map<String, List<Transaction>> getByCategor(@PathVariable String categoryName) {
		Map<String, List<Transaction>> categoryByMonthByCategoryName = service.getCategoryByMonthByCategoryName(categoryName);
		return categoryByMonthByCategoryName;
	}
	
	@RequestMapping("/catstr/{categoryName}")
	public String getByCategorStr(@PathVariable String categoryName) {
		Map<String, Map<String, List<Transaction>>> categoryToMonths = service.getCategoryToMonths();
		return PrinterHelper.printDetailed(categoryName,categoryToMonths,true);
	}

	@RequestMapping("/weekly/{categoryName}")
	public List<Pair<Integer, Double>>[] getWeeklyProjects(@PathVariable String categoryName) {
		List<Transaction> transactions = service.getTransactionByMonthAsList(categoryName);
		List<Pair<Integer, Double>>[] weeklyProjected = PinkiCenter.createWeeklyProjected(transactions, 28, 4, 2);
		PrinterHelper.printProjected(weeklyProjected);
		return weeklyProjected;
	}


	@RequestMapping("/projected")
	public List<Transaction> getProjected() {
		List<Transaction> projectedTransaction = ProjectorHelper.createProjectedTransaction();
		PrinterHelper.print(projectedTransaction);
		return projectedTransaction;
	}
	
}
