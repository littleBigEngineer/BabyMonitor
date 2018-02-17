package com.neo.controller;

import com.neo.model.Account;

public class AccountController {
	
	FirebaseController fc = new FirebaseController();
	boolean goodFlag;

	public Account checkForAccount(String username, String password) {
		System.out.println("Accounts");
		Account a = fc.checkForAccount(username, password);
		return a;
	}
	
//	public void receiveAccount(Account account) {
//		System.out.println(account.getEmail());
//		if(account != null)
//			goodFlag = true;
//		else
//			goodFlag = false;
//	}
//	
//	public boolean sendAccountStat() {
//		return goodFlag;
//	}
}
