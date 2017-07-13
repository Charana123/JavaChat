package com.charana.login_window.utilities;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SecureAlphaNumericStringGenerator {
    //Secure Random is expensive is instantiate therefore are worth reused
    private static SecureRandom random = new SecureRandom();

    public static String get() {
        //Creates a 32 bit random integer and coverts to radix or base 32 | 10 (0-9) + 26 (A-Z) = 36 characters |
        return new BigInteger(128, random).toString(36);
    }
}