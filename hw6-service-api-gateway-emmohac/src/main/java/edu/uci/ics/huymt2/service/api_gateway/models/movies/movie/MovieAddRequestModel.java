package edu.uci.ics.huymt2.service.api_gateway.models.movies.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.GenreModel;
import edu.uci.ics.huymt2.service.api_gateway.models.movies.GenreRequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieAddRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String title;
    @JsonProperty(required = true)
    private String director;
    @JsonProperty(required = true)
    private Integer year;
    private String backdrop_path;
    private Integer budget;
    private String overview;
    private String poster_path;
    private Integer revenue;
    @JsonProperty(required = true)
    private GenreRequestModel[] genres;

    public MovieAddRequestModel() {}

    @JsonCreator
    public MovieAddRequestModel(@JsonProperty(value = "title",required = true) String title,
                                @JsonProperty(value = "director", required = true) String director,
                                @JsonProperty(value = "year", required = true) Integer year,
                                @JsonProperty(value = "backdrop_path") String backdrop_path,
                                @JsonProperty(value = "budget") Integer budget,
                                @JsonProperty(value = "overview") String overview,
                                @JsonProperty(value = "poster_path") String poster_path,
                                @JsonProperty(value = "revenue") Integer revenue,
                                @JsonProperty(value = "genres", required = true) GenreRequestModel[] genres) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.backdrop_path = backdrop_path;
        this.budget = budget;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.genres = genres;
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

    @JsonProperty(value = "genres")
    public GenreRequestModel[] getGenres() {
        return genres;
    }
}
