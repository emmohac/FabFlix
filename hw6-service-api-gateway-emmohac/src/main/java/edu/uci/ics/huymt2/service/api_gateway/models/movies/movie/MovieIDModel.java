package edu.uci.ics.huymt2.service.api_gateway.models.movies.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.GenreModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.star.StarModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "title", "director", "year", "backdrop_path", "budget", "overview", "poster_path", "revenue", "rating", "numVotes", "genres", "stars"})
public class MovieIDModel {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private String title;
    private String director;
    private Integer year;
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;
    @JsonProperty(required = true)
    private Float rating;
    @JsonProperty(required = true)
    private Integer numVotes;
    @JsonProperty(required = true)
    private GenreModel[] genres;
    @JsonProperty(required = true)
    private StarModel[] stars;

    public MovieIDModel() {}
    @JsonCreator
    public MovieIDModel(@JsonProperty(value = "id", required = true) String id,
                        @JsonProperty(value = "title", required = true) String title,
                        @JsonProperty(value = "genres", required = true) GenreModel[] genres,
                        @JsonProperty(value = "stars", required = true) StarModel[] stars) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.stars = stars;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setNumVotes(Integer numVotes) {
        this.numVotes = numVotes;
    }

    @JsonProperty(value = "id")
    public String getId() {
        return id;
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

    @JsonProperty(value = "backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }

    @JsonProperty(value = "budget")
    public Integer getBudget() {
        return budget;
    }

    @JsonProperty(value = "overview")
    public String getOverview() {
        return overview;
    }

    @JsonProperty(value = "poster_path")
    public String getPoster_path() {
        return poster_path;
    }

    @JsonProperty(value = "revenue")
    public Integer getRevenue() {
        return revenue;
    }

    @JsonProperty(value = "rating")
    public Float getRating() {
        return rating;
    }

    @JsonProperty(value = "numVotes")
    public Integer getNumVotes() {
        return numVotes;
    }

    @JsonProperty(value = "genres")
    public GenreModel[] getGenres() {
        return genres;
    }

    @JsonProperty(value = "stars")
    public StarModel[] getStars() {
        return stars;
    }
}
