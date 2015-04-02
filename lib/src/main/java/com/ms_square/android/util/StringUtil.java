package com.ms_square.android.util;

import java.util.UUID;

public class StringUtil {

    /**
     * Generates a variant 2, version 4 (randomly generated number) UUID as per RFC 4122.
     * @return UUID as a string
     */
    public static String getNewUUID() {
        return UUID.randomUUID().toString();
    }
}