package edu.uci.ics.huymt2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResponseMessage;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarMovieResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;

    public StarMovieResponseModel() {}

    @JsonCreator
    public StarMovieResponseModel(@JsonProperty(value ="resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = HelpMe.generateMessageFor(resultCode);
//        switch (resultCode){
//            case ResultCode.JSON_MAP:
//                this.message = ResponseMessage.JSON_MAP;
//                break;
//            case ResultCode.JSON_PARSE:
//                this.message = ResponseMessage.JSON_PARSE;
//                break;
//            case ResultCode.MOVIE_NOT_FOUND:
//                this.message = ResponseMessage.MOVIE_NOT_FOUND;
//                break;
//            case ResultCode.SUCCESSFULLY_ADDED_STAR_MOVIE:
//                this.message = ResponseMessage.SUCCESSFULLY_ADDED_STAR_MOVIE;
//                break;
//            case ResultCode.STAR_MOVIE_NOT_ADDED:
//                this.message = ResponseMessage.STAR_MOVIE_NOT_ADDED;
//                break;
//            case ResultCode.STAR_MOVIE_ALREADY_EXISTED:
//                this.message = ResponseMessage.STAR_MOVIE_ALREADY_EXISTED;
//                break;
//            default:
//        }
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
