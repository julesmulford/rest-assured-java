package com.reqres.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private String token;
    private String error;
    private int id;

    public LoginResponse() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
