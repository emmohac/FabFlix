package edu.uci.ics.huymt2.service.api_gateway.models.billing.order;

import com.fasterxml.jackson.annotation.*;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.TransactionModel;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResponseMessage;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResultCode;
@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"resultCode", "message", "transactions"})
public class OrderRetrieveResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    private TransactionModel[] transactions;

    public OrderRetrieveResponseModel() {}

    @JsonCreator
    public OrderRetrieveResponseModel(@JsonProperty(value = "resultCode",required = true) int resultCode) {
        this.resultCode = resultCode;
        switch (resultCode){
            case ResultCode.JSON_PARSE:
                this.message = ResponseMessage.JSON_PARSE;
                break;
            case ResultCode.JSON_MAP:
                this.message = ResponseMessage.JSON_MAP;
                break;
            case ResultCode.CUSTOMER_NOT_EXIST:
                this.message = ResponseMessage.CUSTOMER_NOT_EXIST;
                break;
            case ResultCode.SUCCESSFULLY_RETREIVED_ORDER:
                this.message = ResponseMessage.SUCCESSFULLY_RETREIVED_ORDER;
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

    @JsonProperty(value = "transactions")
    public TransactionModel[] getTransactions() {
        return transactions;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty(value = "transactions")
    public void setTransactions(TransactionModel[] transactions) {
        this.transactions = transactions;
    }
}
