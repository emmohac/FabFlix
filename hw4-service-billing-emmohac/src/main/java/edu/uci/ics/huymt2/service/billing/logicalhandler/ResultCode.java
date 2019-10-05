package edu.uci.ics.huymt2.service.billing.logicalhandler;

/*
    This class stores the appropriate result code for each endpoints and its logic.
 */
public class ResultCode {
    public static final int JSON_MAP = -2;
    public static final int JSON_PARSE = -3;
    public static final int INVALID_LENGTH = -10;
    public static final int INVALID_FORMAT = -11;
    public static final int INVALID_QUANTITY = 33;
    public static final int DUPLICATE_INSERT = 311;
    public static final int ITEM_NOT_EXISTED = 312;
    public static final int INVALID_CREDITCARD_LENGTH = 321;
    public static final int INVALID_CREDITCARD_VALUE = 322;
    public static final int INVALID_EXPIRATION = 323;
    public static final int CREDITCARD_NOT_EXISTED = 324;
    public static final int DUPLICATE_CREDITCARD_INSERT = 325;
    public static final int CREDITCARD_NOT_FOUND = 331;
    public static final int CUSTOMER_NOT_EXIST = 332;
    public static final int DUPLICATE_CUSTOMER_INSERT = 333;
    public static final int SCART_NOT_FOUND = 341;
    public static final int PAYMENT_FAILED = 342;
    public static final int SUCCESSFULLY_INSERTED = 3100;
    public static final int SUCCESSFULLY_UPDATED = 3110;
    public static final int ITEM_DELETED = 3120;
    public static final int ITEM_RETRIEVED = 3130;
    public static final int ITEM_CLEARED = 3140;
    public static final int SUCCESSFULLY_INSERTED_CREDITCARD = 3200;
    public static final int SUCCESSFULLY_UPDATED_CREDITCARD = 3210;
    public static final int SUCCESSFULLY_DELETED_CREDITCARD = 3220;
    public static final int SUCCESSFULLY_RETRIEVED_CREDITCARD = 3230;
    public static final int SUCCESSFULLY_INSERTED_CUSTOMER = 3300;
    public static final int SUCCESSFULLY_UPDATED_CUSTOMER = 3310;
    public static final int SUCCESSFULLY_RETRIEVED_CUSTOMER = 3320;
    public static final int SUCCESSFULLY_PLACED_ORDER = 3400;
    public static final int SUCCESSFULLY_RETREIVED_ORDER = 3410;
    public static final int TOKEN_NOT_FOUND = 3421;
    public static final int PAYMENT_NOT_COMPLETED = 3422;
    public static final int PAYMENT_COMPLETED = 3420;
}
