package edu.uci.ics.huymt2.service.api_gateway.models.movies.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"movieId", "title", "director", "year", "rating", "numVotes", "hidden"})
public class Movie {
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private String title;
    @JsonProperty(required = true)
    private String director;
    @JsonProperty(required = true)
    private int year;
    @JsonProperty(required = true)
    private float rating;
    @JsonProperty(required = true)
    private int numVotes;
    private Boolean hidden;

    public Movie(){};

    @JsonCreator
    public Movie(@JsonProperty(value = "movieId", required = true) String movieId,
                 @JsonProperty(value = "title", required = true) String title,
                 @JsonProperty(value = "director", required = true) String director,
                 @JsonProperty(value = "year", required = true) int year,
                 @JsonProperty(value = "rating", required = true) float rating,
                 @JsonProperty(value = "numVotes", required = true) int numVotes,
                 @JsonProperty(value = "hidden") Boolean hidden) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.numVotes = numVotes;
        this.hidden = hidden;
    }

    @JsonProperty(value = "movieId")
    public String getMovieId() {
        return movieId;
    }

    @JsonProperty(value = "title")
    public String getTitle() {
        return title;
    }

    @JsonProperty(value = "director")
    public String getDirector() {
        return director;
    }

    @JsonProperty(value = "year")
    public int getYear() {
        return year;
    }

    @JsonProperty(value = "rating")
    public float getRating() {
        return rating;
    }

    @JsonProperty(value = "numVotes")
    public int getNumVotes() {
        return numVotes;
    }

    @JsonProperty(value = "hidden")
    public Boolean getHidden() {
        return hidden;
    }
}
