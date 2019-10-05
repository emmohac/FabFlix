package edu.uci.ics.huymt2.service.billing.models.shoppingcart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveSCartRequestModel {
    @JsonProperty(required = true)
    private String email;

    @JsonCreator
    public RetrieveSCartRequestModel(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }
}
