package edu.uci.ics.huymt2.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;

@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    private String sessionID;

    public LoginResponseModel(){}

    @JsonCreator
    public LoginResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        switch (resultCode){
            case 120:
                this.message = "User logged in successfully";
                break;
            case -12:
                this.message = "Password has invalid length (cannot be empty/null)";
                break;
            case -11:
                this.message = "Email address has invalid format";
                break;
            case -10:
                this.message = "Email address has invalid length";
                break;
            case 11:
                this.message = "Passwords do not match";
                break;
            case -2:
                this.message = "JSON Mapping Exception";
                break;
            case -3:
                this.message = "JSON Parse Exception";
                break;
            case 14:
                this.message = "User not found";
                break;
            default:
        }
    }

    @Override
    public String toString(){
        return "resultCode: "+resultCode + ", message: "+message;
    }
    @JsonProperty(value = "resultCode")
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "sessionID")
    public String getSessionID() { return sessionID; }

    @JsonProperty(value = "message")
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "sessionID")
    public void setSessionID(String sessionID) { this.sessionID = sessionID; }
}
