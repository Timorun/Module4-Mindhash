package com.mindhash.MindhashApp.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserJAXB {
	private String email;
	private String password;

	public UserJAXB() { }

	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}

	public void setEmail(String email) { this.email=email; }

	public void setPassword(String password) { this.password = password; }

}
