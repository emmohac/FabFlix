package edu.uci.ics.huymt2.service.idm.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.idm.core.HelpMe;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifySessionResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    private String sessionID;

    @JsonProperty(value = "sessionID")
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public VerifySessionResponseModel(){}

    @JsonProperty(value = "resultCode")
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message")
    public String getMessage() {
        return message;
    }

    @JsonCreator
    public VerifySessionResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        this.message = HelpMe.generateMessageFor(resultCode);
//        switch (resultCode){
//            case -13:
//                this.message = "Token has invalid length";
//                break;
//            case -11:
//                this.message = "Email address has invalid format";
//                break;
//            case -10:
//                this.message = "Email address has invalid length";
//                break;
//            case 14:
//                this.message = "User not found";
//                break;
//            case -3:
//                this.message = "JSON Parse Exception";
//                break;
//            case -2:
//                this.message = "JSON Mapping Exception";
//                break;
//            case 130:
//                this.message = "Session is active";
//                break;
//            case 131:
//                this.message = "Session is expired";
//                break;
//            case 132:
//                this.message = "Session is closed";
//                break;
//            case 133:
//                this.message = "Session is revoked";
//                break;
//            case 134:
//                this.message = "Session not found";
//                break;
//            default:
//        }
    }
}
