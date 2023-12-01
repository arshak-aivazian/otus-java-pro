package com.example.hw14.web.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class HexFormatUtils {
    private HexFormatUtils() {
    }

    public static String getHash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(value.getBytes());
            String result = HexFormat.of().formatHex(digest.digest());
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
