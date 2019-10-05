package edu.uci.ics.huymt2.service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestModel {
    @JsonProperty(required = true)
    private String email;

    @JsonCreator
    public OrderRequestModel(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }
}
