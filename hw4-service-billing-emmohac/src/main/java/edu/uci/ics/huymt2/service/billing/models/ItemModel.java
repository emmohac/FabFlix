package edu.uci.ics.huymt2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.uci.ics.huymt2.service.billing.core.Item;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemModel {
    @JsonProperty(required = true)
    private String email;
    @JsonProperty(required = true)
    private String movieId;
    @JsonProperty(required = true)
    private Integer quantity;
    @JsonProperty(required = true)
    private Float unit_price;
    @JsonProperty(required = true)
    private Float discount;
    @JsonProperty(required = true)
    private String saleDate;

    public ItemModel() {}

    @JsonCreator
    public ItemModel(@JsonProperty(value = "email", required = true) String email,
                      @JsonProperty(value = "movieId", required = true) String movieId,
                      @JsonProperty(value = "quantity", required = true) Integer quantity){
        this.email = email;
        this.movieId = movieId;
        this.quantity = quantity;
    }

    @JsonCreator
    public ItemModel(@JsonProperty(value = "email", required = true) String email,
                @JsonProperty(value = "movieId", required = true) String movieId,
                @JsonProperty(value = "quantity", required = true) Integer quantity,
                @JsonProperty(value = "unit_price", required = true) Float unit_price,
                @JsonProperty(value = "discount", required = true) Float discount,
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
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty(value = "unit_price")
    public Float getUnit_price() {
        return unit_price;
    }

    @JsonProperty(value = "discount")
    public Float getDiscount() {
        return discount;
    }

    @JsonProperty(value = "saleDate")
    public String getSaleDate() {
        return saleDate;
    }
}
