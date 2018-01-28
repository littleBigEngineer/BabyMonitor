package com.neo.model;

public class Account {
    private String firstName, lastName, uid, phone;

    public Account(String firstName, String lastName, String uid, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.uid = uid;
        this.phone = phone;
    }

    public Account(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}