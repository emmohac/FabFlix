package edu.uci.ics.huymt2.service.api_gateway.models.movies.movie;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.Model;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.GenreModel;
import edu.uci.ics.huymt2.service.api_gateway.utilities.*;
@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieResponseModel extends Model {
    @JsonProperty(required = true)
    private int resultCode;
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private GenreModel[] genres;

    public MovieResponseModel(){}

    @JsonCreator
    public MovieResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode) {
        this.resultCode = resultCode;
        switch (resultCode){
            case ResultCode.JSON_MAP:
                this.message = ResponseMessage.JSON_MAP;
                break;
            case ResultCode.JSON_PARSE:
                this.message = ResponseMessage.JSON_PARSE;
                break;
            case ResultCode.GENRE_NOT_ADDED:
                this.message = ResponseMessage.GENRE_NOT_ADDED;
                break;
            case ResultCode.SUCCESSFULLY_ADDED_GENRE:
                this.message = ResponseMessage.SUCCESSFULLY_ADDED_GENRE;
                break;
            case ResultCode.MOVIE_NOT_REMOVED:
                this.message = ResponseMessage.MOVIE_NOT_REMOVED;
                break;
            case ResultCode.SUCCESSFULLY_REMOVED_MOVIE:
                this.message = ResponseMessage.SUCCESSFULLY_REMOVED_MOVIE;
                break;
            case ResultCode.SUCCESSFULLY_RETRIEVED_GENRE:
                this.message = ResponseMessage.SUCCESSFULLY_RETRIEVED_GENRE;
                break;
            case ResultCode.MOVIE_ALREADY_REMOVED:
                this.message = ResponseMessage.MOVIE_ALREADY_REMOVED;
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

    @JsonProperty(value = "genres")
    public GenreModel[] getGenres() {
        return genres;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGenres(GenreModel[] genres) {
        this.genres = genres;
    }
}
