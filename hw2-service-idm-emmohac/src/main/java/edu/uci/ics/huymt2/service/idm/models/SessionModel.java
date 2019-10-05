package edu.uci.ics.huymt2.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String sessionID;

    @JsonCreator
    public SessionModel(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "sessionID", required = true) String sessionID ) {
        this.email = email;
        this.sessionID = sessionID;
    }

    @Override
    public String toString() {
        return "Email: " + email + ", SessionID: " + sessionID;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("sessionID")
    public String getSessionID() {
        return sessionID;
    }
}
