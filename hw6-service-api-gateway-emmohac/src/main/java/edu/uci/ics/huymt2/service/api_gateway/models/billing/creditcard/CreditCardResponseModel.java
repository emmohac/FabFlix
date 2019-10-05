package edu.uci.ics.huymt2.service.api_gateway.models.billing.creditcard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResultCode;
import edu.uci.ics.huymt2.service.api_gateway.utilities.*;

@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCardResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    public CreditCardResponseModel() {}

    @JsonCreator
    public CreditCardResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        switch (resultCode){
            case ResultCode.JSON_PARSE:
                this.message = ResponseMessage.JSON_PARSE;
                break;
            case ResultCode.JSON_MAP:
                this.message = ResponseMessage.JSON_MAP;
                break;
            case ResultCode.INVALID_CREDITCARD_LENGTH:
                this.message = ResponseMessage.INVALID_CREDITCARD_LENGTH;
                break;
            case ResultCode.INVALID_CREDITCARD_VALUE:
                this.message = ResponseMessage.INVALID_CREDITCARD_VALUE;
                break;
            case ResultCode.INVALID_EXPIRATION:
                this.message = ResponseMessage.INVALID_EXPIRATION;
                break;
            case ResultCode.DUPLICATE_CREDITCARD_INSERT:
                this.message = ResponseMessage.DUPLICATE_CREDITCARD_INSERT;
                break;
            case ResultCode.SUCCESSFULLY_INSERTED_CREDITCARD:
                this.message = ResponseMessage.SUCCESSFULLY_INSERTED_CREDITCARD;
                break;
            case ResultCode.CREDITCARD_NOT_EXISTED:
                this.message = ResponseMessage.CREDITCARD_NOT_EXIST;
                break;
            case ResultCode.SUCCESSFULLY_UPDATED_CREDITCARD:
                this.message = ResponseMessage.SUCCESSFULLY_UPDATED_CREDITCARD;
                break;
            case ResultCode.SUCCESSFULLY_DELETED_CREDITCARD:
                this.message = ResponseMessage.SUCCESSFULLY_DELETED_CREDITCARD;
                break;
            default:
        }
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
