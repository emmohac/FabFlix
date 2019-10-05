package edu.uci.ics.huymt2.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionRequestModel {
    @JsonProperty(required = true)
    private String email;

    @JsonCreator
    public SessionRequestModel(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
}
