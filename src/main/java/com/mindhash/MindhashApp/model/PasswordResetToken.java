package com.mindhash.MindhashApp.model;

import java.util.concurrent.atomic.AtomicInteger;

public class PasswordResetToken {
    private String token;
    private User user;
    private int tokenId;
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);

    public PasswordResetToken(){
        this.tokenId = ID_GENERATOR.getAndIncrement();
    }

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public int getTokenId() { return tokenId; }

}
