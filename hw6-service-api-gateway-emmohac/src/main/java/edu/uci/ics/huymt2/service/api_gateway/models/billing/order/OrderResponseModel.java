package edu.uci.ics.huymt2.service.api_gateway.models.billing.order;

import com.fasterxml.jackson.annotation.*;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.OrderDetailModel;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResponseMessage;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResultCode;
@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"resultCode", "message", "items"})
public class OrderResponseModel extends Model {
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

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty(value = "items")
    public void setItems(OrderDetailModel[] items) {
        this.items = items;
    }
}
