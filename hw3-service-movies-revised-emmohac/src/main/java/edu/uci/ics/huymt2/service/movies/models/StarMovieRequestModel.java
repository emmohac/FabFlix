package edu.uci.ics.huymt2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarMovieRequestModel {
    @JsonProperty(required = true)
    private String starid;
    @JsonProperty(required = true)
    private String movieid;

    public StarMovieRequestModel() {}

    @JsonCreator
    public StarMovieRequestModel(@JsonProperty(value = "starid", required = true) String starId,
                                 @JsonProperty(value = "movieid", required = true) String movieId) {
        this.starid = starId;
        this.movieid = movieId;
    }

    @JsonProperty(value = "starid")
    public String getStarid() {
        return starid;
    }

    @JsonProperty(value = "movieid")
    public String getMovieid() {
        return movieid;
    }
}
