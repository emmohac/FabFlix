package edu.uci.ics.huymt2.service.api_gateway.models.billing.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResponseMessage;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResultCode;
@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPlaceResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    private String redirectURL;
    private String token;

    public OrderPlaceResponseModel() {}

    @JsonCreator
    public OrderPlaceResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        switch (resultCode){
            case ResultCode.JSON_MAP:
                this.message = ResponseMessage.JSON_MAP;
                break;
            case ResultCode.JSON_PARSE:
                this.message = ResponseMessage.JSON_PARSE;
                break;
            case ResultCode.CUSTOMER_NOT_EXIST:
                this.message = ResponseMessage.CUSTOMER_NOT_EXIST;
                break;
            case ResultCode.PAYMENT_FAILED:
                this.message = ResponseMessage.PAYMENT_FAILED;
                break;
            case ResultCode.SCART_NOT_FOUND:
                this.message = ResponseMessage.SCART_NOT_FOUND;
                break;
            case ResultCode.SUCCESSFULLY_PLACED_ORDER:
                this.message = ResponseMessage.SUCCESSFULLY_PLACED_ORDER;
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

    @JsonProperty(value = "redirectURL")
    public String getRedirectURL() {
        return redirectURL;
    }

    @JsonProperty(value = "token")
    public String getToken() {
        return token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty(value = "redirectURL")
    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    @JsonProperty(value = "token")
    public void setToken(String token) {
        this.token = token;
    }
}
