package org.cardanofoundation.merkle.core;

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
