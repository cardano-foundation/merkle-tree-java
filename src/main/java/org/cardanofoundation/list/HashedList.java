package org.cardanofoundation.list;

import lombok.val;
import org.cardanofoundation.util.Hashing;

import java.util.List;
import java.util.function.Function;

public record HashedList<T>(List<T> list, byte[] hash) {

    public static <T> HashedList<byte[]> create(List<T> items, Function<T, byte[]> hasher) {
        val hashedItems = items.stream().map(hasher).toList();

        val zero = Hashing.sha2_256(new byte[0]);

        val hash = hashedItems.stream().reduce(zero, Hashing::combineHash);

        return new HashedList<>(hashedItems, hash);
    }

}
