package edu.uci.ics.huymt2.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private char[] password;

    public LoginRequestModel(){}

    @JsonCreator
    public LoginRequestModel(@JsonProperty(value = "email", required = true) String email,
                             @JsonProperty(value = "password", required = true) char[] password){
        this.email = email;
        this.password = password;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "password")
    public char[] getPassword() {
        return password;
    }
}
