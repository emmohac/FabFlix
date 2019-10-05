package edu.uci.ics.huymt2.service.api_gateway.models.billing.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.AmountModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.ItemModel;
import edu.uci.ics.huymt2.service.api_gateway.models.billing.TransactionFeeModel;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
    private String transactionId;
    private String state;
    private AmountModel amount;
    private TransactionFeeModel transaction_fee;
    private String create_time;
    private String update_time;
    private ItemModel[] items;

    public Transaction(String transactionId, String state, AmountModel amount, TransactionFeeModel transaction_fee, String create_time, String update_time, ItemModel[] items) {
        this.transactionId = transactionId;
        this.state = state;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_time;
        this.items = items;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getState() {
        return state;
    }

    public AmountModel getAmount() {
        return amount;
    }

    public TransactionFeeModel getTransaction_fee() {
        return transaction_fee;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public ItemModel[] getItems() {
        return items;
    }
}
