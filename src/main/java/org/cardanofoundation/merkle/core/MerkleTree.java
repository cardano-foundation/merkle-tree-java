package org.cardanofoundation.merkle.core;

public interface MerkleTree {

    default byte[] rootHash() {
        if (this instanceof MerkleEmpty) {
            return new byte [] { };
        }

        if (this instanceof MerkleLeaf ml) {
            return ml.getHash();
        }
        if (this instanceof MerkleNode mn) {
            return mn.getHash();
        }

        throw new IllegalStateException("Unexpected value:" + this.getClass().getName());
    }

    default int size() {
        if (this instanceof MerkleEmpty) {
            return 0;
        }

        if (this instanceof MerkleLeaf ml) {
            return 1;
        }
        if (this instanceof MerkleNode mn) {
            return mn.getLeft().size() + mn.getRight().size();
        }

        throw new IllegalStateException("Unexpected value:" + this.getClass().getName());
    }

}
