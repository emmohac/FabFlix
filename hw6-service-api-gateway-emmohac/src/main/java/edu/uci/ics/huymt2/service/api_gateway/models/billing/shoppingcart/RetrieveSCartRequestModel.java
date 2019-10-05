package edu.uci.ics.huymt2.service.api_gateway.models.billing.shoppingcart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;
@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveSCartRequestModel extends RequestModel {
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
