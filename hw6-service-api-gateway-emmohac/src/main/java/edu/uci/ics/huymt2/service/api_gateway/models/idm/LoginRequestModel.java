package edu.uci.ics.huymt2.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequestModel extends RequestModel {
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
