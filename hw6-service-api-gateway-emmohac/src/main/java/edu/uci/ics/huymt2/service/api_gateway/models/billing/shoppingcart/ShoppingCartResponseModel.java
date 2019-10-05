package edu.uci.ics.huymt2.service.api_gateway.models.billing.shoppingcart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.utilities.*;
@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    @JsonCreator
    public ShoppingCartResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
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
            case ResultCode.INVALID_QUANTITY:
                this.message = ResponseMessage.INVALID_QUANTITY;
                break;
            case ResultCode.DUPLICATE_INSERT:
                this.message = ResponseMessage.DUPLICATE_INSERT;
                break;
            case ResultCode.SUCCESSFULLY_INSERTED:
                this.message = ResponseMessage.SUCCESSFULLY_INSERTED;
                break;
            case ResultCode.ITEM_NOT_EXISTED:
                this.message = ResponseMessage.ITEM_NOT_EXISTED;
                break;
            case ResultCode.SUCCESSFULLY_UPDATED:
                this.message = ResponseMessage.SUCCESSFULLY_UPDATED;
                break;
            case ResultCode.ITEM_DELETED:
                this.message = ResponseMessage.ITEM_DELETED;
                break;
            case ResultCode.ITEM_CLEARED:
                this.message = ResponseMessage.ITEM_CLEARED;
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
