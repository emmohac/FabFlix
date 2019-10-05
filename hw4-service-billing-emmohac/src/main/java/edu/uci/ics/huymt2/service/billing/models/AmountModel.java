package edu.uci.ics.huymt2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"total", "currency"})
public class AmountModel {
    @JsonProperty(required = true)
    private String total;
    @JsonProperty(required = true)
    private String currency;

    public AmountModel() {}
    @JsonCreator
    public AmountModel(@JsonProperty(value= "total", required = true) String total,
                       @JsonProperty(value= "currency", required = true) String currency) {
        this.total = total;
        this.currency = currency;
    }

    @JsonProperty(value = "total")
    public String getTotal() {
        return total;
    }

    @JsonProperty(value = "currency")
    public String getCurrency() {
        return currency;
    }
}
