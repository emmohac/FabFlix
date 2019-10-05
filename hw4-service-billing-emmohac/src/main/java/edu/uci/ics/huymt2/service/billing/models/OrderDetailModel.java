package edu.uci.ics.huymt2.service.billing.models;

import com.fasterxml.jackson.annotation.*;
import edu.uci.ics.huymt2.service.billing.core.OrderDetail;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"email", "movieId", "quantity", "saleDate"})
public class OrderDetailModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private int quantity;
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PST")
    private Date saleDate;

    public OrderDetailModel() {}

    @JsonCreator
    public OrderDetailModel(@JsonProperty(value = "email", required = true) String email,
                            @JsonProperty(value = "movieId", required = true) String movieId,
                            @JsonProperty(value = "quantity", required = true) int quantity,
                            @JsonProperty(value = "saleDate", required = true) Date saleDate) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.saleDate = saleDate;
    }

    public static OrderDetailModel buildModelFromObject(OrderDetail o){
        return new OrderDetailModel(o.getEmail(), o.getMovieId(), o.getQuantity(), o.getSaleDate());
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
