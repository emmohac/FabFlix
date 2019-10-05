package edu.uci.ics.huymt2.service.api_gateway.models.movies.star;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.utilities.*;
@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarSearchResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private StarModel[] stars;

    public StarSearchResponseModel() {}

    @JsonCreator
    public StarSearchResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;

        switch (resultCode){
            case ResultCode.STAR_FOUND:
                this.message = ResponseMessage.STAR_FOUND;
                break;
            case ResultCode.STAR_NOT_FOUND:
                this.message = ResponseMessage.STAR_NOT_FOUND;
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

    @JsonProperty(value = "stars")
    public StarModel[] getStars() {
        return stars;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStars(StarModel[] stars) {
        this.stars = stars;
    }
}
