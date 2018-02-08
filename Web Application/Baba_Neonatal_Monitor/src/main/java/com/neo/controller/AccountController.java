package com.neo.controller;

public class AccountController {
	
	FirebaseController fc = new FirebaseController();
	boolean goodFlag;

	public void checkForAccount(String username, String password) {
		fc.checkForAccount(username, password);
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
