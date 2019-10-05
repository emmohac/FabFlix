package edu.uci.ics.huymt2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResponseMessage;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"resultCode", "message", "movieid", "genreid"})
public class MovieAddResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private String movieid;
    @JsonProperty(required = true)
    private int[] genreid;

    public MovieAddResponseModel() {}

    @JsonCreator
    public MovieAddResponseModel(@JsonProperty(value = "resultCode",required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = HelpMe.generateMessageFor(resultCode);
//        switch (resultCode){
//            case ResultCode.JSON_MAP:
//                this.message = ResponseMessage.JSON_MAP;
//                break;
//            case ResultCode.JSON_PARSE:
//                this.message = ResponseMessage.JSON_PARSE;
//                break;
//            case ResultCode.SUCCESSFULLY_ADDED_MOVIE:
//                this.message = ResponseMessage.SUCCESSFULLY_ADDED_MOVIE;
//                break;
//            case ResultCode.MOVIE_NOT_ADDED:
//                this.message = ResponseMessage.MOVIE_NOT_ADDED;
//                break;
//            case ResultCode.MOVIE_ALREADY_EXISTED:
//                this.message = ResponseMessage.MOVIE_ALREADY_EXISTED;
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

    @JsonProperty(value = "movieid")
    public String getMovieid() {
        return movieid;
    }

    @JsonProperty(value = "genreid")
    public int[] getGenres() {
        return genreid;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public void setGenreid(int[] genreid) {
        this.genreid = genreid;
    }
}
