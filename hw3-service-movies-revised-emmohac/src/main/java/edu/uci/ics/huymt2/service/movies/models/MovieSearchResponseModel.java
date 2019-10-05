package edu.uci.ics.huymt2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.uci.ics.huymt2.service.movies.core.HelpMe;
import edu.uci.ics.huymt2.service.movies.core.ResponseMessage;
import edu.uci.ics.huymt2.service.movies.core.ResultCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"resultCode, message, movies"})
public class MovieSearchResponseModel {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private MovieModel[] movies;

    public MovieSearchResponseModel() {}

    @JsonCreator
    public MovieSearchResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        this.message = HelpMe.generateMessageFor(resultCode);
//        switch (resultCode){
//            case ResultCode.MOVIE_FOUND:
//                this.message = ResponseMessage.MOVIE_FOUND;
//                break;
//            case ResultCode.MOVIE_NOT_FOUND:
//                this.message = ResponseMessage.MOVIE_NOT_FOUND;
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

    @JsonProperty(value = "movies")
    public MovieModel[] getMovies() {
        return movies;
    }

    //@JsonProperty(value = "message")
    public void setMessage(String message) {
        this.message = message;
    }

    //@JsonProperty(value = "movies")
    public void setMovies(MovieModel[] movies) {
        this.movies = movies;
    }
}
