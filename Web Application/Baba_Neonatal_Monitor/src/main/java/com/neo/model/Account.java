package com.neo.model;

import java.util.ArrayList;

public class Account {
	private String email, firstName, lastName, phone, password, username;
	private ArrayList<String> associatedDevices = new ArrayList<>();

	public Account(String email, String firstName, String lastName, String phone, String password, String username) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.password = password;
		this.username = username;
	}

	public Account(){

	}
	
	//Account functionality
	
	public void addAssociatedDevice(String deviceId) {
		if(associatedDevices.size() < 7)
			associatedDevices.add(deviceId);
	}
	
	public void removeAssociatedDevice(String deviceId) {
		associatedDevices.remove(deviceId);
	}
	
	
	//Getters and Setters	

	public ArrayList<String> getAssociatedDevices() {
		return associatedDevices;
	}

	public void setAssociatedDevices(ArrayList<String> associatedDevices) {
		this.associatedDevices = associatedDevices;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

}