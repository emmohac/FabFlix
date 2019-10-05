package edu.uci.ics.huymt2.service.api_gateway.models.idm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;

@JsonIgnoreProperties(value = {"dataValid"})
public class VerifyPrivilegeResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    public VerifyPrivilegeResponseModel(){}

    @JsonProperty(value = "resultCode")
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message")
    public String getMessage() {
        return message;
    }

    @JsonCreator
    public VerifyPrivilegeResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        switch (resultCode){
            case -14:
                this.message = "Privilege level out of valid range";
                break;
            case -11:
                this.message = "Email address has invalid format";
                break;
            case -10:
                this.message = "Email address has invalid length";
                break;
            case -3:
                this.message = "JSON Parse Exception";
                break;
            case -2:
                this.message = "JSON Mapping Exception";
                break;
            case 140:
                this.message = "User has sufficient privilege level";
                break;
            case 141:
                this.message = "User has insufficient privilege level";
                break;
            default:
        }
    }
}
