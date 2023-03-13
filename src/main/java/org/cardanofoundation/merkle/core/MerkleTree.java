package org.cardanofoundation.merkle.core;

import lombok.val;
import org.cardanofoundation.merkle.util.Hashing;

import java.util.List;
import java.util.function.Function;

public class MerkleTree {

    public static MerkleElement createFromHashes(List<byte[]> items) {
        return doFrom(items, items.size());
    }

    public static <T> MerkleElement createFromItems(List<T> items, Function<T, byte[]> serialiserFn) {
        return createFromHashes(items.stream().map(serialiserFn::apply)
                .toList());
    }

    // TODO this can look nicer when JDK supports pattern matching on lists
    private static MerkleElement doFrom(List<byte[]> items, int len) {
        if (items.isEmpty()) {
            return MerkleEmpty.EMPTY;
        }
        if (items.size() == 1) {
            return new MerkleLeaf(items.stream().findFirst().orElseThrow());
        }

        val cutOff = len / 2;
        val left = doFrom(items.subList(0, cutOff), cutOff);
        val right = doFrom(items.subList(cutOff, items.size()), (len - cutOff));

        val hash = Hashing.combineHash(left.elementHash(), right.elementHash());

        return new MerkleNode(hash, left, right);
    }

}
