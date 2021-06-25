package com.mindhash.MindhashApp.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserRegJAXB extends UserJAXB {
	private String confirmPassword;
	
	public UserRegJAXB() {
		super();
	}
	
	public String getConfirmPassword() {
		return this.confirmPassword;
	}
}
