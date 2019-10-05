package edu.uci.ics.huymt2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResponseMessage;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieIDSearchResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private MovieIDModel movie;

    @JsonCreator
    public MovieIDSearchResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = HelpMe.generateMessageFor(resultCode);
//        switch (resultCode){
//            case ResultCode.MOVIE_FOUND:
//                this.message = ResponseMessage.MOVIE_FOUND;
//                break;
//            case ResultCode.MOVIE_NOT_FOUND:
//                this.message = ResponseMessage.MOVIE_NOT_FOUND;
//                break;
//            case ResultCode.MOVIE_ALREADY_REMOVED:
//                this.message = ResponseMessage.MOVIE_ALREADY_REMOVED;
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

    @JsonProperty(value = "movie")
    public MovieIDModel getMovie() {
        return movie;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMovie(MovieIDModel movie) {
        this.movie = movie;
    }
}
