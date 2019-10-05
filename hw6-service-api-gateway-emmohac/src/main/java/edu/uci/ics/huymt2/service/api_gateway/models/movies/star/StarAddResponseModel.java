package edu.uci.ics.huymt2.service.api_gateway.models.movies.star;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResponseMessage;
import edu.uci.ics.huymt2.service.api_gateway.utilities.ResultCode;
@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarAddResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    public StarAddResponseModel() {}

    @JsonCreator
    public StarAddResponseModel(@JsonProperty(value = "resultCode",required = true) int resultCode) {
        this.resultCode = resultCode;
        switch (resultCode){
            case ResultCode.JSON_MAP:
                this.message = ResponseMessage.JSON_MAP;
                break;
            case ResultCode.JSON_PARSE:
                this.message = ResponseMessage.JSON_PARSE;
                break;
            case ResultCode.STAR_ALREADY_EXISTED:
                this.message = ResponseMessage.STAR_ALREADY_EXISTED;
                break;
            case ResultCode.STAR_NOT_ADDED:
                this.message = ResponseMessage.STAR_NOT_ADDED;
                break;
            case ResultCode.SUCCESSFULLY_ADDED_STAR:
                this.message = ResponseMessage.SUCCESSFULLY_ADDED_STAR;
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
