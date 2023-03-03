package org.cardanofoundation.merkle;

public record MerkleProofHash(MerkleHash hash, MerkleProofItem direction) {

    public enum MerkleProofItem {
        LEFT,
        RIGHT,
    }

}
