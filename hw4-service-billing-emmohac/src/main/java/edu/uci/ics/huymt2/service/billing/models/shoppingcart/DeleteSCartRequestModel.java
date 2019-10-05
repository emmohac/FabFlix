package edu.uci.ics.huymt2.service.billing.models.shoppingcart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteSCartRequestModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String movieId;

    @JsonCreator
    public DeleteSCartRequestModel(@JsonProperty(value = "email", required = true) String email,
                                   @JsonProperty(value = "movieId", required = true) String movieId) {
        this.email = email;
        this.movieId = movieId;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "movieId")
    public String getMovieId() {
        return movieId;
    }
}
