package edu.uci.ics.huymt2.service.api_gateway.models.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatingRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private Float rating;

    public RatingRequestModel(){}

    @JsonCreator
    public RatingRequestModel(@JsonProperty(value = "id", required = true) String id,
                              @JsonProperty(value = "rating", required = true) Float rating) {
        this.id = id;
        this.rating = rating;
    }

    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }

    @JsonProperty(value = "rating")
    public Float getRating() {
        return rating;
    }
}
