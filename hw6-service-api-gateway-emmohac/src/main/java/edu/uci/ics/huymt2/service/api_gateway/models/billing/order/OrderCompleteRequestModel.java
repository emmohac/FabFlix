package edu.uci.ics.huymt2.service.api_gateway.models.billing.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.huymt2.service.api_gateway.models.RequestModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderCompleteRequestModel extends RequestModel {
    @JsonProperty(required = true)
    private String paymentId;
    @JsonProperty(required = true)
    private String token;
    @JsonProperty(required = true)
    private String payerId;

    public OrderCompleteRequestModel() {}

    @JsonCreator
    public OrderCompleteRequestModel(@JsonProperty(value = "paymentId",required = true) String paymentId,
                                     @JsonProperty(value = "token", required = true) String token,
                                     @JsonProperty(value ="payerId", required = true) String payerId) {
        this.paymentId = paymentId;
        this.token = token;
        this.payerId = payerId;
    }

    @JsonProperty(value = "paymentId")
    public String getPaymentId() {
        return paymentId;
    }

    @JsonProperty(value = "token")
    public String getToken() {
        return token;
    }

    @JsonProperty(value = "payerId")
    public String getPayerId() {
        return payerId;
    }
}
