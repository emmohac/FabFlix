package edu.uci.ics.huymt2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionFeeModel {
    @JsonProperty(required = true)
    private String value;
    @JsonProperty(required = true)
    private String currency;

    public TransactionFeeModel() {}

    @JsonCreator
    public TransactionFeeModel(String value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    @JsonProperty(value = "value")
    public String getValue() {
        return value;
    }

    @JsonProperty(value = "currency")
    public String getCurrency() {
        return currency;
    }
}
