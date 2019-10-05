package edu.uci.ics.huymt2.service.idm.core;

public class ResultCode {
    public static final int PASSWORD_EMPTY = -12;
    public static final int PASSWORD_INVALID_LENGTH = 12;
    public static final int EMAIL_INVALID_FORMAT = -11;
    public static final int EMAIL_INVALID_LENGTH = -10;
    public static final int JSON_MAP = -3;
    public static final int JSON_PARSE = -2;
    public static final int PASSWORD_INVALID_REQUIREMENT = 13;
    public static final int EMAIL_ALREADY_USED = 16;
    public static final int SUCCESSFULLY_REGISTERED = 110;
    public static final int PASSWORD_NOT_MATCH = 11;
    public static final int USER_NOT_FOUND = 14;
    public static final int SUCCESSFULLY_LOGIN = 120;
    public static final int SESSION_ACTIVE = 130;
    public static final int SESSION_EXPIRED = 131;
    public static final int SESSION_CLOSED = 132;
    public static final int SESSION_REVOKED = 133;
    public static final int SESSION_NOT_FOUND = 134;
    public static final int PLEVEL_INVALID = -14;
    public static final int SESSION_INVALID_LENGTH = -13;
    public static final int SUFFICIENT_PRIVILEGE = 140;
    public static final int INSUFFICIENT_PRIVILEGE = 141;
}
