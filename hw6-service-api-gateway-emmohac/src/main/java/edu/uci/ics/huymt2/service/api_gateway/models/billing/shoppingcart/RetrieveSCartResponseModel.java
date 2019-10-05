package edu.uci.ics.huymt2.service.api_gateway.models.billing.shoppingcart;

import com.fasterxml.jackson.annotation.*;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.*;
import edu.uci.ics.huymt2.service.api_gateway.utilities.*;
@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"resultCode", "message", "items"})
public class RetrieveSCartResponseModel extends Model {
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

    public void setItems(ItemModel[] items) {
        this.items = items;
    }
}
