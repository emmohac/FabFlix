package edu.uci.ics.huymt2.service.billing.logicalhandler;

/*
    This class stores message that correlates to the result code. Each result code will different message.
 */
public class ResponseMessage {
    public static final String INVALID_FORMAT = "Email address has invalid format.";
    public static final String INVALID_LENGTH = "Email address has invalid length.";
    public static final String JSON_PARSE = "JSON Parse Exception.";
    public static final String JSON_MAP = "JSON Mapping Exception.";
    public static final String INVALID_QUANTITY = "Quantity has invalid value.";
    public static final String DUPLICATE_INSERT = "Duplicate insertion.";
    public static final String SUCCESSFULLY_INSERTED = "Shopping cart item inserted successfully.";
    public static final String ITEM_NOT_EXISTED = "Shopping item does not exist.";
    public static final String SUCCESSFULLY_UPDATED = "Shopping cart item updated successfully.";
    public static final String ITEM_DELETED = "Shopping cart item deleted successfully.";
    public static final String ITEM_RETRIEVED = "Shopping cart retrieved successfully.";
    public static final String ITEM_CLEARED = "Shopping cart cleared successfully.";
    public static final String INVALID_CREDITCARD_LENGTH = "Credit card ID has invalid length.";
    public static final String INVALID_CREDITCARD_VALUE = "Credit card ID has invalid value.";
    public static final String INVALID_EXPIRATION = "expiration has invalid value";
    public static final String DUPLICATE_CREDITCARD_INSERT = "Duplicate insertion.";
    public static final String SUCCESSFULLY_INSERTED_CREDITCARD = "Credit card inserted successfully.";
    public static final String CREDITCARD_NOT_EXIST = "Credit card does not exist.";
    public static final String SUCCESSFULLY_UPDATED_CREDITCARD = "Credit card updated successfully.";
    public static final String SUCCESSFULLY_DELETED_CREDITCARD = "Credit card deleted successfully.";
    public static final String SUCCESSFULLY_RETRIEVED_CREDITCARD = "Credit card retrieved successfully.";
    public static final String SCART_NOT_FOUND = "Shopping cart for this customer not found";
    public static final String SUCCESSFULLY_PLACED_ORDER = "Order placed successfully";
    public static final String CUSTOMER_NOT_EXIST = "Customer does not exist";
    public static final String SUCCESSFULLY_RETREIVED_ORDER = "Orders retrieved successfully";
    public static final String SUCCESSFULLY_INSERTED_CUSTOMER = "Customer inserted successfully.";
    public static final String SUCCESSFULLY_UPDATED_CUSTOMER = "Customer updated successfully.";
    public static final String SUCCESSFULLY_RETRIEVED_CUSTOMER = "Customer retrieved successfully.";
    public static final String CREDITCARD_NOT_FOUND = "Credit card ID not found.";
    public static final String PAYMENT_FAILED = "Create payment failed.";
    public static final String TOKEN_NOT_FOUND = "Token not found.";
    public static final String PAYMENT_NOT_COMPLETED = "Payment cannot be completed.";
    public static final String PAYMENT_COMPLETED = "Payment is completed successfully.";
}
