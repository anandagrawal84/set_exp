package com.app.settleexpenses.domain;

public class Email {
	private String email;
	private String type;
	
	public Email(String name, String type) {
		this.email = name;
		this.type = type;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getType() {
		return type;
	}
}
