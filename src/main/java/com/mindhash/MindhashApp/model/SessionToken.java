package com.mindhash.MindhashApp.model;

import java.security.SecureRandom;
import java.util.Base64;

import com.mindhash.MindhashApp.dao.SessionTokenDao;

public class SessionToken {
	public String email;
	public String sessiontoken;
	
	public SessionToken(String email) {
		sessiontoken = createToken();
		this.email = email;
	}
	
	public String createToken() {
		while (true) {
			SecureRandom random = new SecureRandom();
			Base64.Encoder encoder = Base64.getUrlEncoder();
			byte[] randomBytes = new byte[24];
			random.nextBytes(randomBytes);
			String token = encoder.encodeToString(randomBytes);
			if (SessionTokenDao.getUser(token) == null) {
				return token;
			}
		}
	}
}
