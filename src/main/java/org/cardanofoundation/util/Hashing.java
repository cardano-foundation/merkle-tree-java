package org.cardanofoundation.util;

import lombok.val;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Hashing {

    public static byte[] combineHash(byte[] left, byte[] right) {
        return sha2_256(Bytes.concat(left, right));
    }

    public static byte[] sha2_256(String value) {
        return sha2_256(value.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] sha2_256(byte[] buffer) {
        try {
            val digest = MessageDigest.getInstance("SHA-256");

            return digest.digest(buffer);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
