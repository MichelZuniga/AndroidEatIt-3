package com.example.ayomide.androideatit;

public class Token {
    private String token;
    private Boolean serverToken;

    public Token(String token, Boolean serverToken) {
        this.token = token;
        this.serverToken = serverToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getServerToken() {
        return serverToken;
    }

    public void setServerToken(Boolean serverToken) {
        this.serverToken = serverToken;
    }
}
