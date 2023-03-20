package org.cardanofoundation.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import org.cardanofoundation.util.Hashing;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
@Getter
@ToString
public record HashedList<T>(List<T> list, byte[] hash) {

    public static <T> HashedList<T> create(List<T> items, Function<T, byte[]> serialiserFn) {
        val hashedItems = io.vavr.collection.List.ofAll(items.stream()).map(serialiserFn);

        val zero = Hashing.sha2_256(new byte[0]);

        val hash = hashedItems.foldRight(zero, Hashing::combineHash);

        return new HashedList(hashedItems.toJavaList(), hash);
    }

}
