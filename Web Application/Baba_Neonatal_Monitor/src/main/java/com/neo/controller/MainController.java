package com.neo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController{

	@RequestMapping(value = "/dashboard", method=RequestMethod.GET)
	public String dashboard(HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		return "dashboard_bootstrap";
	}

	@RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
	public ResponseEntity<String> getCurrentUser(HttpSession session){
		String user = session.getAttribute("username").toString();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody void logout(HttpSession session) {
		session.removeAttribute("username");
	}
}