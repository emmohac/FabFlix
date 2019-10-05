package edu.uci.ics.huymt2.service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResponseMessage;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResultCode;
import edu.uci.ics.huymt2.service.billing.models.OrderDetailModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"resultCode", "message", "items"})
public class OrderResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private OrderDetailModel[] items;

    @JsonCreator
    public OrderResponseModel(@JsonProperty(value = "id", required = true) int resultCode) {
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
            case ResultCode.SCART_NOT_FOUND:
                this.message = ResponseMessage.SCART_NOT_FOUND;
                break;
            case ResultCode.SUCCESSFULLY_PLACED_ORDER:
                this.message = ResponseMessage.SUCCESSFULLY_PLACED_ORDER;
                break;
            case ResultCode.SUCCESSFULLY_RETREIVED_ORDER:
                this.message = ResponseMessage.SUCCESSFULLY_RETREIVED_ORDER;
                break;
            case ResultCode.PAYMENT_COMPLETED:
                this.message = ResponseMessage.PAYMENT_COMPLETED;
                break;
            case ResultCode.PAYMENT_NOT_COMPLETED:
                this.message = ResponseMessage.PAYMENT_NOT_COMPLETED;
                break;
            case ResultCode.TOKEN_NOT_FOUND:
                this.message = ResponseMessage.TOKEN_NOT_FOUND;
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

    @JsonProperty(value = "items")
    public OrderDetailModel[] getItems() {
        return items;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setItems(OrderDetailModel[] items) {
        this.items = items;
    }
}
