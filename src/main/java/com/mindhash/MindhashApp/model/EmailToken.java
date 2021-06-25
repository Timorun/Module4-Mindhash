package com.mindhash.MindhashApp.model;

import java.util.concurrent.atomic.AtomicInteger;

public class EmailToken {
    private String token;
    private UserJAXB user;
    private int tokenId;
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);

    public EmailToken(){
        this.tokenId = ID_GENERATOR.getAndIncrement();
    }

    public EmailToken(String token, UserJAXB user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public UserJAXB getUser() { return user; }

    public void setUser(UserJAXB user) { this.user = user; }

    public int getTokenId() { return tokenId; }

}
