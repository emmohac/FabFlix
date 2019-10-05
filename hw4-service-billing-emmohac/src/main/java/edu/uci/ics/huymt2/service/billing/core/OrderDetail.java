package edu.uci.ics.huymt2.service.billing.core;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"email", "movieId", "quantity", "saleDate"})
public class OrderDetail {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private int quantity;
    @JsonProperty(required = true)
    private Date saleDate;

    @JsonCreator
    public OrderDetail(@JsonProperty(value = "email", required = true) String email,
                       @JsonProperty(value = "movieId", required = true) String movieId,
                       @JsonProperty(value = "quantity", required = true) int quantity,
                       @JsonProperty(value = "saleDate", required = true) Date saleDate) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.saleDate = saleDate;
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

    @JsonProperty(value = "saleDate")
    public Date getSaleDate() {
        return saleDate;
    }
}
