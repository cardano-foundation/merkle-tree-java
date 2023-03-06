package org.cardanofoundation.merkle;

public record MerkleProofHash(byte[] hash, MerkleProofItem direction) {

    public enum MerkleProofItem {
        LEFT,
        RIGHT,
    }

}
