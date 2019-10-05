package edu.uci.ics.huymt2.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.uci.ics.huymt2.service.movies.core.Movie;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("{movieId, title, director, year, rating, numVotes}")
public class MovieModel {
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private String title;
    @JsonProperty(required = true)
    private String director;
    @JsonProperty(required = true)
    private Integer year;
    @JsonProperty(required = true)
    private Float rating;
    @JsonProperty(required = true)
    private Integer numVotes;
    private Boolean hidden;

    public MovieModel(){}

    @JsonCreator
    public MovieModel(@JsonProperty(value = "movieId", required = true) String movieId,
                 @JsonProperty(value = "title", required = true) String title,
                 @JsonProperty(value = "director", required = true) String director,
                 @JsonProperty(value = "year", required = true) Integer year,
                 @JsonProperty(value = "rating", required = true) Float rating,
                 @JsonProperty(value = "numVotes", required = true) Integer numVotes) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.numVotes = numVotes;
    }

    public static MovieModel buildModelFromObject(Movie movie){
        return new MovieModel(movie.getMovieId(), movie.getTitle(), movie.getDirector(), movie.getYear(), movie.getRating(), movie.getNumVotes());
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
    public Integer getYear() {
        return year;
    }

    @JsonProperty(value = "rating")
    public Float getRating() {
        return rating;
    }

    @JsonProperty(value = "numVotes")
    public Integer getNumVotes() {
        return numVotes;
    }

    @JsonProperty(value = "hidden")
    public Boolean getHidden() {
        return hidden;
    }
}
