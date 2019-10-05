package edu.uci.ics.huymt2.service.api_gateway.models.billing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.core.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"email", "movieId", "quantity"})
public class ItemModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private int quantity;
    @JsonProperty(required = true)
    private float unit_price;
    @JsonProperty(required = true)
    private float discount;
    @JsonProperty(required = true)
    private String saleDate;

    public ItemModel() {}

    @JsonCreator
    public ItemModel(@JsonProperty(value = "email", required = true) String email,
                     @JsonProperty(value = "movieId", required = true) String movieId,
                     @JsonProperty(value = "quantity", required = true) int quantity){
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    @JsonCreator
    public ItemModel(@JsonProperty(value = "email", required = true) String email,
                     @JsonProperty(value = "movieId", required = true) String movieId,
                     @JsonProperty(value = "quantity", required = true) int quantity,
                     @JsonProperty(value = "unit_price", required = true) float unit_price,
                     @JsonProperty(value = "discount", required = true) float discount,
                     @JsonProperty(value = "saleDate", required = true) String saleDate) {
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
        this.discount = discount;
        this.unit_price = unit_price;
        this.saleDate = saleDate;
    }

    public static ItemModel buildModelFromObject(Item i){
        return new ItemModel(i.getEmail(), i.getMovieId(), i.getQuantity());
    }

    public static ItemModel buildModelFromAdvancedObject(Item i){
        return new ItemModel(i.getEmail(), i.getMovieId(), i.getQuantity(), i.getUnit_price(), i.getDiscount(), i.getSaleDate());
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

    @JsonProperty(value = "unit_price")
    public float getUnit_price() {
        return unit_price;
    }

    @JsonProperty(value = "discount")
    public float getDiscount() {
        return discount;
    }

    @JsonProperty(value = "saleDate")
    public String getSaleDate() {
        return saleDate;
    }
}
