package com.neo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.neo.model.Login;

@Controller
public class MainController {
	
	AccountController ac = new AccountController();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("login", new Login()); 
		return "index";
	}
	
	@RequestMapping("/login")
	public String login(Login login) {
		ac.checkForAccount(login.getEmail(), login.getPassword());
	    return "index";
	}
	

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard() {
		return "dashboard";
	}
}
