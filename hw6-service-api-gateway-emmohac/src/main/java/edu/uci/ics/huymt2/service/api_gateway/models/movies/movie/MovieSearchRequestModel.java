package edu.uci.ics.huymt2.service.api_gateway.models.movies.movie;

import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

public class MovieSearchRequestModel extends RequestModel {
    private String title;
    private String genre;
    private Integer year;
    private String director;
    private Boolean hidden;
    private Integer limit;
    private Integer offset;
    private String orderby;
    private String direction;

    public MovieSearchRequestModel(String title, String genre, Integer year, String director, Boolean hidden, Integer limit, Integer offset, String orderby, String direction) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.director = director;
        this.hidden = hidden;
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.direction = direction;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public String getOrderby() {
        return orderby;
    }

    public String getDirection() {
        return direction;
    }
}
