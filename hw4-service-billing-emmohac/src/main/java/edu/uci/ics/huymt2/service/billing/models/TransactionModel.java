package edu.uci.ics.huymt2.service.billing.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionModel {
    @JsonProperty(required = true)
    private String transactionId;
    @JsonProperty(required = true)
    private String state;
    @JsonProperty(required = true)
    private AmountModel amount;
    @JsonProperty(required = true)
    private TransactionFeeModel transaction_fee;
    @JsonProperty(required = true)
    private String create_time;
    @JsonProperty(required = true)
    private String update_time;
    private ItemModel[] items;

    public TransactionModel() {}

    @JsonCreator
    public TransactionModel(@JsonProperty(value = "transactionId", required = true) String transactionId,
                            @JsonProperty(value = "state", required = true) String state,
                            @JsonProperty(value = "amount", required = true) AmountModel amount,
                            @JsonProperty(value = "transaction_fee", required = true) TransactionFeeModel transaction_fee,
                            @JsonProperty(value = "create_time", required = true) String create_time,
                            @JsonProperty(value = "update_time", required = true) String update_time,
                            @JsonProperty(value = "items", required = true) ItemModel[] items) {
        this.transactionId = transactionId;
        this.state = state;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_time;
        this.items = items;
    }

    @JsonProperty(value = "transactionId")
    public String getTransactionId() {
        return transactionId;
    }

    @JsonProperty(value = "state")
    public String getState() {
        return state;
    }

    @JsonProperty(value = "amount")
    public AmountModel getAmount() {
        return amount;
    }

    @JsonProperty(value = "transaction_fee")
    public TransactionFeeModel getTransaction_fee() {
        return transaction_fee;
    }

    @JsonProperty(value = "create_time")
    public String getCreate_time() {
        return create_time;
    }

    @JsonProperty(value = "update_time")
    public String getUpdate_time() {
        return update_time;
    }

    @JsonProperty(value = "items")
    public ItemModel[] getItems() {
        return items;
    }
}
