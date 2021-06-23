package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.model.PasswordResetToken;

import java.util.HashMap;
import java.util.Map;

public enum PasswordResetTokenDao {
    instance;
    private Map<String, PasswordResetToken> contentProvider = new HashMap<>();

    private PasswordResetTokenDao(){}

    public Map<String, PasswordResetToken> getModel(){
        return contentProvider;
    }
}
