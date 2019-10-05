package edu.uci.ics.huymt2.service.api_gateway.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = {"dataValid"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoContentResponseModel {
    @JsonProperty(required = true)
    private String message;
    @JsonProperty(required = true)
    private int delay;
    @JsonProperty(required = true)
    private String transactionID;


    public NoContentResponseModel() {}

    @JsonCreator
    public NoContentResponseModel(@JsonProperty(value = "delay", required = true) int delay,
                                  @JsonProperty(value = "transactionID", required = true) String transactionID) {
        this.message = "Request received.";
        this.delay = delay;
        this.transactionID = transactionID;
    }

    @JsonProperty(value = "message", required = true)
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "delay", required = true)
    public int getDelay() {
        return delay;
    }

    @JsonProperty(value = "transactionID", required = true)
    public String getTransactionID() {
        return transactionID;
    }
}
