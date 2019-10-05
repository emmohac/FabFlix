package edu.uci.ics.huymt2.service.api_gateway.models.billing.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveCustomerRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String email;

    public RetrieveCustomerRequestModel(){}

    @JsonCreator
    public RetrieveCustomerRequestModel(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }
}
