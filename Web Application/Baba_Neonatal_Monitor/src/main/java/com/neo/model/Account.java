package com.neo.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class Account {
	
	@NotNull
	@Length(min=1, max=30)
	private String email;
	
	@NotNull
	@Length(min=1, max=20)
	private String firstName;
	
	@NotNull
	@Length(min=1, max=20)
	private String lastName;
	
	@NotNull
	@Length(min=10, max=10)
	private String phone;
	
	@NotNull
	@Length(min=8, max=20)
	private String password;
	
	@NotNull
	@Length(min=1, max=20)
	private String username;

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

	//Getters and Setters	

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