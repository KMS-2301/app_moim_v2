package org.edupoll.controller;

import org.edupoll.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
	
	@Autowired
	SearchService searchService;
	
	@GetMapping("/search")
	public String searchResultHandle(@RequestParam(required = false) String q, Model model) {
		if(q == null) {
			return "search/form";
		}else {
			model.addAttribute("result", searchService.getUsersMatchedQuery(q));
			model.addAttribute("query" , q);
			return "search/result";
		}
	}
}
