package com.neo.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.neo.model.Account;
import com.neo.model.Device;
import com.neo.model.Login;

@Controller
public class MainController {
	
	AccountController ac = new AccountController();
	FirebaseController fc = new FirebaseController();
	String currentUser = "";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("login", new Login()); 
		return "index";
	}
	
	@RequestMapping("/")
	public String login(Model model, Login login) {
		Account account = ac.checkForAccount(login.getEmail(), login.getPassword());
		if(account != null)
			currentUser = account.getUsername();
		
		System.out.println("Current:" + currentUser);
		
		ArrayList<Device> devices = fc.getDeviceList(currentUser);
		model.addAttribute("devices", devices);
		return "dashboard";
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard() {
		return "dashboard";
	}
}
