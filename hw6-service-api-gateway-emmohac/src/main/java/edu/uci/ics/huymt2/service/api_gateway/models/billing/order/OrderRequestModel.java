package edu.uci.ics.huymt2.service.api_gateway.models.billing.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestModel extends RequestModel {
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
