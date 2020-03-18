package com.example.newboedoserver.Model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.InstanceIdResult;

public class Token {

   /* private String token;
    public boolean isServerToken;

    //  public Token(Task<InstanceIdResult> instanceId, boolean isServerToken) {
    //}

    public Token(String token, boolean isServerToken) {
        this.token = token;
        this.isServerToken = isServerToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerToken() {
        return isServerToken;
    }

    public void setServerToken(boolean serverToken) {
        isServerToken = serverToken;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", isServerToken=" + isServerToken +
                '}';
    }
}*/

    public String token;
    public boolean serverToken;

    public Token() {
    }

    public Token(String token, boolean serverToken) {
        this.token = token;
        this.serverToken = serverToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerToken() {
        return serverToken;
    }

    public void setServerToken(boolean serverToken) {
        this.serverToken = serverToken;
    }
}



