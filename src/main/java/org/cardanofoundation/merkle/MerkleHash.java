package org.cardanofoundation.merkle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import static java.nio.charset.StandardCharsets.UTF_8;

@EqualsAndHashCode
@Getter
public final class MerkleHash {

    private byte[] value;

    private MerkleHash() {
    }

    public static MerkleHash create(byte[] buffer) {
        var hash = new MerkleHash();
        hash.computeHash(buffer);

        return hash;
    }

    public static MerkleHash create(String buffer) {
        return create(buffer.getBytes(UTF_8));
    }

    public static MerkleHash create(MerkleHash left, MerkleHash right) {
        return create(concatenate(left.getValue(), right.getValue()));
    }

    private void computeHash(byte[] buffer) {
        try {
            val digest = MessageDigest.getInstance("SHA-256");
            this.value = digest.digest(buffer);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] concatenate(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }

    @Override
    public String toString() {
        return "MerkleHash{" +
                "value(hex)=" + HexFormat.of().formatHex(value) +
                '}';
    }

}
