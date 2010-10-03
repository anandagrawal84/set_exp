package com.app.settleexpenses.domain;

public class Phone {

	private String number;
	private String type;
	
	public Phone(String name, String type) {
		super();
		this.number = name;
		this.type = type;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getType() {
		return type;
	}
}
