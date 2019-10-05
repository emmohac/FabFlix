package edu.uci.ics.huymt2.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HashPassRequestModel {
    private String password;

    @JsonCreator
    public HashPassRequestModel(@JsonProperty(value = "password", required = true) String password) {
        this.password = password;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }
}
