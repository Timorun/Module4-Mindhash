package com.mindhash.MindhashApp.model;

public class User {
	private int id;
    private String email;
    private String password;
    private String salt;
    private String token;
    private String sessionexpire;
    private boolean isadmin;

    public User() {}

    public User(String email, String password, String salt) {
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public User(String email, String sessionexpire, boolean isadmin) {
        this.email = email;
        this.sessionexpire = sessionexpire;
        this.isadmin = isadmin;
    }
    
    public int getId() { 
    	return id; 
    }
    
    public void setId(int id) { 
    	this.id = id; 
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSalt() {
        return salt;
    }

    public String getToken() {
    	return token;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSessionexpire() {
    	return sessionexpire;
    }

    public boolean isIsadmin() {
    	return isadmin;
    }
}
