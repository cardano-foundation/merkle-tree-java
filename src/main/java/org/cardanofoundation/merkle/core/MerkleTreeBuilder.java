package org.cardanofoundation.merkle.core;

import lombok.val;
import org.cardanofoundation.merkle.util.Bytes;
import org.cardanofoundation.merkle.util.Hashing;

import java.util.List;

public class MerkleTreeBuilder {

    public static MerkleTree create(List<byte[]> items) {
        return doFrom(items, items.size());
    }

    private static MerkleTree doFrom(List<byte[]> items, int len) {
        if (items.isEmpty()) {
            return MerkleEmpty.EMPTY;
        }
        if (items.size() == 1) {
            return new MerkleLeaf(items.get(0));
        }

        val cutOff = len / 2;
        val left = doFrom(items.subList(0, cutOff), cutOff);
        val right = doFrom(items.subList(cutOff, items.size()), len - cutOff);

        val hash = Hashing.sha2_256(Bytes.concat(left.rootHash(), right.rootHash()));

        return new MerkleNode(hash, left, right);
    }

}
