package edu.uci.ics.huymt2.service.api_gateway.models.billing.shoppingcart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private int quantity;

    public ShoppingCartRequestModel() {}

    @JsonCreator
    public ShoppingCartRequestModel(@JsonProperty(value = "email", required = true) String email,
                                    @JsonProperty(value = "movieId", required = true) String movieId,
                                    @JsonProperty(value = "quantity", required = true) int quantity) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "movieId")
    public String getMovieId() {
        return movieId;
    }

    @JsonProperty(value = "quantity")
    public int getQuantity() {
        return quantity;
    }
}
