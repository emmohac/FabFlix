package edu.uci.ics.huymt2.service.billing.models.shoppingcart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResponseMessage;
import edu.uci.ics.huymt2.service.billing.logicalhandler.ResultCode;
import edu.uci.ics.huymt2.service.billing.models.ItemModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"resultCode", "message", "items"})
public class RetrieveSCartResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private ItemModel[] items;

    @JsonCreator
    public RetrieveSCartResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode){
        this.resultCode = resultCode;
        switch (resultCode){
            case ResultCode.INVALID_FORMAT:
                this.message = ResponseMessage.INVALID_FORMAT;
                break;
            case ResultCode.INVALID_LENGTH:
                this.message = ResponseMessage.INVALID_LENGTH;
                break;
            case ResultCode.JSON_PARSE:
                this.message = ResponseMessage.JSON_PARSE;
                break;
            case ResultCode.JSON_MAP:
                this.message = ResponseMessage.JSON_MAP;
                break;
            case ResultCode.ITEM_NOT_EXISTED:
                this.message = ResponseMessage.ITEM_NOT_EXISTED;
                break;
            case ResultCode.ITEM_RETRIEVED:
                this.message = ResponseMessage.ITEM_RETRIEVED;
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
    public ItemModel[] getItems() {
        return items;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setItems(ItemModel[] items) {
        this.items = items;
    }
}
