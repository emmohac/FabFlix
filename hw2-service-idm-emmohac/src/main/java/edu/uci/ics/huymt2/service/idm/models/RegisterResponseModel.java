package edu.uci.ics.huymt2.service.idm.models;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.idm.core.HelpMe;


public class RegisterResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    public RegisterResponseModel(){}

    @JsonCreator
    public RegisterResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        this.message = HelpMe.generateMessageFor(resultCode);
//        switch (resultCode){
//            case 110:
//                this.message = "User registered successfully";
//                break;
//            case 16:
//                this.message = "Email already in use";
//                break;
//            case 12:
//                this.message = "Password does not meet length requirements";
//                break;
//            case 13:
//                this.message = "Password does not meet character requirements";
//                break;
//            case -12:
//                this.message = "Password has invalid length (cannot be empty/null)";
//                break;
//            case -11:
//                this.message = "Email address has invalid format";
//                break;
//            case -10:
//                this.message = "Email address has invalid length";
//                break;
//            case -2:
//                this.message = "JSON Mapping Exception";
//                break;
//            case -3:
//                this.message = "JSON Parse Exception";
//                break;
//            case -1:
//            default:
//        }
    }

    @Override
    public String toString(){
        return "resultCode: "+resultCode + ", message: "+message;
    }

    @JsonProperty(value = "resultCode")
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message")
    public String getMessage() {
        return message;
    }
}
