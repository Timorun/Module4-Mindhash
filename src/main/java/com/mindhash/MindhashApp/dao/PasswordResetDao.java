package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.model.PasswordResetToken;

import java.util.HashMap;
import java.util.Map;

public enum PasswordResetDao {
    instance;
    private Map<Integer, PasswordResetToken> contentProvider = new HashMap<>();

    private PasswordResetDao(){}

    public Map<Integer, PasswordResetToken> getModel(){
        return contentProvider;
    }
}
