package org.cardanofoundation.merkle.core;

import org.cardanofoundation.merkle.util.Bytes;
import org.cardanofoundation.merkle.util.Hashing;

public interface MerkleTree {

    default byte[] rootHash() {
        return switch (this) {
            case MerkleEmpty me -> new byte [] { };
            case MerkleLeaf ml -> ml.getHash();
            case MerkleNode mn -> mn.getHash();
            default -> throw new IllegalStateException("Unexpected value.");
        };
    }

}
