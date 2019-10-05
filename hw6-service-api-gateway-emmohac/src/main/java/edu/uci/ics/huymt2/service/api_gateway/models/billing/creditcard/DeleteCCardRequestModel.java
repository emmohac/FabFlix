package edu.uci.ics.huymt2.service.api_gateway.models.billing.creditcard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteCCardRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String id;

    @JsonCreator
    public DeleteCCardRequestModel(@JsonProperty(value = "id", required = true) String id) {
        this.id = id;
    }

    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }
}
