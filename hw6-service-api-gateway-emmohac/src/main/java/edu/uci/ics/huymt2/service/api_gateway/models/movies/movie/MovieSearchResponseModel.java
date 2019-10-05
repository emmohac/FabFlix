package edu.uci.ics.huymt2.service.api_gateway.models.movies.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.utilities.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"dataValid"})
public class MovieSearchResponseModel extends Model {
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
        switch (resultCode){
            case ResultCode.MOVIE_FOUND:
                this.message = ResponseMessage.MOVIE_FOUND;
                break;
            case ResultCode.MOVIE_NOT_FOUND:
                this.message = ResponseMessage.MOVIE_NOT_FOUND;
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

    @JsonProperty(value = "movies")
    public MovieModel[] getMovies() {
        return movies;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMovies(MovieModel[] movies) {
        this.movies = movies;
    }
}
