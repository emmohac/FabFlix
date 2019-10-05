package edu.uci.ics.huymt2.service.api_gateway.models.movies.star;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StarSearchRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private Integer birthYear;
    @JsonProperty(required = true)
    private String movieTitle;
    @JsonProperty(required = true)
    private Integer limit;
    @JsonProperty(required = true)
    private Integer offset;
    @JsonProperty(required = true)
    private String orderby;
    @JsonProperty(required = true)
    private String direction;

    public StarSearchRequestModel() {}

    @JsonCreator
    public StarSearchRequestModel(@JsonProperty(value = "name", required = true) String name,
                                  @JsonProperty(value = "birthYear", required = true) Integer birthYear,
                                  @JsonProperty(value = "movieTitle", required = true) String movieTitle,
                                  @JsonProperty(value = "limit", required = true) Integer limit,
                                  @JsonProperty(value = "offset", required = true) Integer offset,
                                  @JsonProperty(value = "orderby", required = true) String orderby,
                                  @JsonProperty(value = "direction", required = true) String direction) {
        this.name = name;
        this.birthYear = birthYear;
        this.movieTitle = movieTitle;
        this.limit = limit;
        this.offset = offset;
        this.orderby = orderby;
        this.direction = direction;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }

    @JsonProperty(value = "birthYear")
    public Integer getBirthYear() {
        return birthYear;
    }

    @JsonProperty(value = "movieTitle")
    public String getMovieTitle() {
        return movieTitle;
    }

    @JsonProperty(value = "limit")
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty(value = "offset")
    public Integer getOffset() {
        return offset;
    }

    @JsonProperty(value = "orderby")
    public String getOrderby() {
        return orderby;
    }

    @JsonProperty(value = "direction")
    public String getDirection() {
        return direction;
    }
}
