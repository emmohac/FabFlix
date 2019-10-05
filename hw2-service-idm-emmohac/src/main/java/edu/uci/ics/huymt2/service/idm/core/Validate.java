package edu.uci.ics.huymt2.service.idm.core;

import java.util.Arrays;

public class Validate {
    public static boolean isEmailFormatted(String e){
        int countAt = 0;
        for (int i = 0; i < e.length(); ++i)
            if (e.charAt(i) == '@')
                ++countAt;

        if (countAt != 1)
            return false;

        String[] toCheck = e.split("@");

        if (toCheck[0].isEmpty() || toCheck[1].isEmpty() || !isWellFormatted(toCheck[0]) || !isWellFormatted(toCheck[1]))
            return false;

        return true;
    }

    public static boolean isWellFormatted(String prefix){
        if (prefix.endsWith("-") || prefix.startsWith(".")
                || prefix.startsWith("-") || prefix.endsWith(".")
                || hasContiguousDot(prefix))
            return false;
        return true;
    }

    public static boolean hasContiguousDot(String s){
        for (int i = 0; i < s.length()-1; ++i)
            if (s.indexOf(i) == '.' && s.indexOf(i+1) == '.')
                return true;

        return false;
    }

    public static boolean isPasswordFormat(char[] p){
        int upperCase = 0,
                lowerCase = 0,
                number = 0,
                special = 0;

        for (int i = 0; i < p.length; ++i)
            if (Character.isUpperCase(p[i]))
                ++upperCase;
            else if (Character.isLowerCase(p[i]))
                ++lowerCase;
            else if (Character.isDigit(p[i]))
                ++number;
            else
                ++special;

        return (upperCase > 0 && lowerCase > 0 && number > 0 && special > 0);
    }

    public static String getHashedPass(byte[] hashedPassword) {
        StringBuffer buf = new StringBuffer();
        for (byte b : hashedPassword) {
            buf.append(format(Integer.toHexString(Byte.toUnsignedInt(b))));
        }
        return buf.toString();
    }

    public static String format(String binS) {
        int length = 2 - binS.length();
        char[] padArray = new char[length];
        Arrays.fill(padArray, '0');
        String padString = new String(padArray);
        return padString + binS;
    }
}
