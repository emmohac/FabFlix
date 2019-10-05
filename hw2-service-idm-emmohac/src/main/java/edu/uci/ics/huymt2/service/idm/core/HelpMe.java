package edu.uci.ics.huymt2.service.idm.core;

public class HelpMe {
    public static boolean verifyResultCode(int resultCode){
        switch (resultCode){
            case ResultCode.EMAIL_INVALID_FORMAT:
                return false;
            case ResultCode.EMAIL_INVALID_LENGTH:
                return false;
            case ResultCode.PASSWORD_INVALID_LENGTH:
                return false;
            case ResultCode.USER_NOT_FOUND:
                return false;
            case ResultCode.SESSION_INVALID_LENGTH:
                return false;
            case ResultCode.EMAIL_ALREADY_USED:
                return true;
            case ResultCode.SESSION_ACTIVE:
                return true;
            case ResultCode.SESSION_CLOSED:
                return true;
            case ResultCode.SESSION_EXPIRED:
                return true;
            case ResultCode.SESSION_REVOKED:
                return true;
            case ResultCode.SESSION_NOT_FOUND:
                return true;
            case ResultCode.PASSWORD_NOT_MATCH:
                return true;
            case ResultCode.SUCCESSFULLY_LOGIN:
                return true;
            case ResultCode.SUCCESSFULLY_REGISTERED:
                return true;
            case ResultCode.INSUFFICIENT_PRIVILEGE:
                return true;
            case ResultCode.SUFFICIENT_PRIVILEGE:
                return true;
            default:
                return false;
        }
    }
    public static String generateMessageFor(int resultCode){
        switch (resultCode){
            case ResultCode.EMAIL_ALREADY_USED:
                return ResponseMessage.EMAIL_ALREADY_USED;
            case ResultCode.EMAIL_INVALID_FORMAT:
                return ResponseMessage.EMAIL_INVALID_FORMAT;
            case ResultCode.JSON_MAP:
                return ResponseMessage.JSON_MAP;
            case ResultCode.JSON_PARSE:
                return ResponseMessage.JSON_PARSE;
            case ResultCode.PLEVEL_INVALID:
                return ResponseMessage.PLEVEL_INVALID;
            case ResultCode.SESSION_ACTIVE:
                return ResponseMessage.SESSION_ACTIVE;
            case ResultCode.SESSION_CLOSED:
                return ResponseMessage.SESSION_CLOSED;
            case ResultCode.SESSION_EXPIRED:
                return ResponseMessage.SESSION_EXPIRED;
            case ResultCode.SESSION_NOT_FOUND:
                return ResponseMessage.SESSION_NOT_FOUND;
            case ResultCode.SESSION_REVOKED:
                return ResponseMessage.SESSION_REVOKED;
            case ResultCode.EMAIL_INVALID_LENGTH:
                return ResponseMessage.EMAIL_INVALID_LENGTH;
            case ResultCode.PASSWORD_INVALID_LENGTH:
                return ResponseMessage.PASSWORD_INVALID_LENGTH;
            case ResultCode.PASSWORD_INVALID_REQUIREMENT:
                return ResponseMessage.PASSWORD_INVALID_REQUIREMENT;
            case ResultCode.PASSWORD_NOT_MATCH:
                return ResponseMessage.PASSWORD_NOT_MATCH;
            case ResultCode.PASSWORD_EMPTY:
                return ResponseMessage.PASSWORD_EMPTY;
            case ResultCode.SUCCESSFULLY_LOGIN:
                return ResponseMessage.SUCCESSFULLY_LOGIN;
            case ResultCode.INSUFFICIENT_PRIVILEGE:
                return ResponseMessage.INSUFFICIENT_PRIVILEGE;
            case ResultCode.SUCCESSFULLY_REGISTERED:
                return ResponseMessage.SUCCESSFULLY_REGISTERED;
            case ResultCode.SUFFICIENT_PRIVILEGE:
                return ResponseMessage.SUFFICIENT_PRIVILEGE;
            case ResultCode.SESSION_INVALID_LENGTH:
                return ResponseMessage.SESSION_INVALID_LENGTH;
            case ResultCode.USER_NOT_FOUND:
                return ResponseMessage.USER_NOT_FOUND;
            default:
                return "Unknown code";
        }
    }
}
