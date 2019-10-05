package edu.uci.ics.huymt2.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyPrivilegeRequestModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private int plevel;

    public VerifyPrivilegeRequestModel(){}

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "plevel")
    public int getPlevel() {
        return plevel;
    }

    @JsonCreator
    public VerifyPrivilegeRequestModel(@JsonProperty(value = "email", required = true) String email,
                                       @JsonProperty(value = "plevel", required = true) int plevel) {
        this.email = email;
        this.plevel = plevel;
    }
}
