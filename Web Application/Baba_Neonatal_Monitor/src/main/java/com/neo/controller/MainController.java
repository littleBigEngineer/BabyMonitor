package com.neo.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.neo.model.Account;
import com.neo.model.Device;
import com.neo.model.Login;

@Controller
public class MainController{

	AccountController ac = new AccountController();
	FirebaseController fc = new FirebaseController();
	DeviceController dc = new DeviceController();
	SensorController sc = new SensorController();
	Thread mainThread = Thread.currentThread();
	
	String currentUser = "";
	boolean first = true;

	@RequestMapping(value = "/")
	public String index(Model model) throws IOException {
		if(first) {
			fc.initFirebase();
			first = false;
		}
		model.addAttribute("login", new Login()); 
		model.addAttribute("account", new Account()); 
		return "index";
	}

	@RequestMapping(value = "/login")
	public String login(Model model, Login login) {
		Account account = ac.checkForAccount(login.getUsername(), login.getPassword());
		if(account != null)
			currentUser = account.getUsername();
		ArrayList<Device> devices = dc.getDeviceList(currentUser);
		model.addAttribute("devices", devices);
		return "dashboard";
	}
	
	@RequestMapping(value = "/getRegistered", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<ArrayList<String>>> getRegistered(){
		ArrayList<ArrayList<String>> output = fc.getUsernames();
		return new ResponseEntity<>(output, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/customerdata", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<String>> getCustomerData(){
		sc.getTemperatureReading();
		ArrayList<String> info = new ArrayList<>();
		int temp = (int) Math.round(sc.getTemperatureReading());
		String message = "" + temp;
		String coStatus = sc.getCarbonMonoxideStatus();

		info.add("Device Name");
		info.add(message);
		info.add(coStatus);

		return new ResponseEntity<>(info, HttpStatus.OK);
	}

	@RequestMapping(value = "/createAccount")
	public String createAccount(Model model, Account account) {
		Account check = ac.checkForAccount(account.getUsername(), account.getPassword());
		if(check == null) {
			ac.createAccount(account);
			model.addAttribute("login", new Login()); 
			model.addAttribute("account", new Account());
		}
		else {
			model.addAttribute("login", new Login()); 
			model.addAttribute("account", new Account());
			model.addAttribute("error", "Account Already Exists");
		}
		return "index";
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard() {
		return "dashboard";
	}
}