package edu.uci.ics.huymt2.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifySessionRequestModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String sessionID;

    public VerifySessionRequestModel(){}
    
    @JsonCreator
    public VerifySessionRequestModel(@JsonProperty(value = "email", required = true) String email,
                                     @JsonProperty(value = "sessionID", required = true) String sessionID){
        this.email = email;
        this.sessionID = sessionID;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "sessionID")
    public String getSessionID() {
        return sessionID;
    }
}
