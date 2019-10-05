package edu.uci.ics.huymt2.service.billing.models.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResponseMessage;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResultCode;
import edu.uci.ics.huymt2.service.billing.models.customer.CustomerModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveCustomerResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private CustomerModel customer;

    @JsonCreator
    public RetrieveCustomerResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        switch (resultCode){
            case ResultCode.CUSTOMER_NOT_EXIST:
                this.message = ResponseMessage.CUSTOMER_NOT_EXIST;
                break;
            case ResultCode.SUCCESSFULLY_RETRIEVED_CUSTOMER:
                this.message = ResponseMessage.SUCCESSFULLY_RETRIEVED_CUSTOMER;
                break;
            case ResultCode.JSON_MAP:
                this.message = ResponseMessage.JSON_MAP;
                break;
            case ResultCode.JSON_PARSE:
                this.message = ResponseMessage.JSON_PARSE;
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

    @JsonProperty(value = "customer")
    public CustomerModel getCustomer() {
        return customer;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }
}
