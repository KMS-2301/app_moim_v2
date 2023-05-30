package org.edupoll.controller;

import org.edupoll.model.entity.UserDetail;
import org.edupoll.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;

@Controller
public class PrivateController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	UserService userService;

	@GetMapping("/private/delete")
	public String deleteUesrHandle(@SessionAttribute String logonId, HttpSession session) {
		boolean rst = userService.deleteSpecificUser(logonId);
		session.invalidate();
		
		return "private/goodbye";
	}

	@GetMapping("/private/modify")
	public String showPrivateModifyForm(@SessionAttribute String logonId, Model model) {
		UserDetail savedDetail = userService.findSpecificSavedDetail(logonId);
		model.addAttribute("savedDetail", savedDetail);

		return "private/modify-form";
	}

	@PostMapping("/private/modify")
	public String privateModifyHanlde(@SessionAttribute String logonId, UserDetail detail, Model model) {
		boolean rst = userService.modifySpecificUserDetail(logonId, detail);
		logger.debug("privateModifyHanlde .. {} ", rst);
		return "redirect:/private/modify";
	}
}
