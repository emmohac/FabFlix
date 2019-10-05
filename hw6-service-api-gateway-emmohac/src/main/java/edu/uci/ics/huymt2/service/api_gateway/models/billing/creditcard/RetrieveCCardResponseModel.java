package edu.uci.ics.huymt2.service.api_gateway.models.billing.creditcard;

import com.fasterxml.jackson.annotation.*;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.core.CreditCardInfo;
import edu.uci.ics.huymt2.service.api_gateway.utilities.*;

@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"resultCode", "message", "creditcard"})
public class RetrieveCCardResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private CreditCardInfo creditcard;

    @JsonCreator
    public RetrieveCCardResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
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
            case ResultCode.CREDITCARD_NOT_EXISTED:
                this.message = ResponseMessage.CREDITCARD_NOT_EXIST;
                break;
            case ResultCode.SUCCESSFULLY_RETRIEVED_CREDITCARD:
                this.message = ResponseMessage.SUCCESSFULLY_RETRIEVED_CREDITCARD;
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

    @JsonProperty(value = "creditcard")
    public CreditCardInfo getCreditcard() {
        return creditcard;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @JsonProperty(value = "creditcard")
    public void setCreditcard(CreditCardInfo creditcard) {
        this.creditcard = creditcard;
    }
}
