package org.cardanofoundation.merkle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class MerkleNode {

    private MerkleHash hash;
    private MerkleNode leftNode;
    private MerkleNode rightNode;
    private MerkleNode parent;

    public MerkleNode() {
    }

    public MerkleNode(MerkleHash hash) {
        this.hash = hash;
    }

    public MerkleNode(MerkleNode left, MerkleNode right) {
        this.leftNode = left;
        this.rightNode = right;
        this.leftNode.parent = this;
        if (this.rightNode != null) this.rightNode.parent = this;

        this.computeHash();
    }

    public boolean isLeaf() {
        return this.leftNode == null && this.rightNode == null;
    }

    public void computeHash() {
        if (this.rightNode == null) {
            this.hash = this.leftNode.hash;
        } else {
            this.hash = MerkleHash.create(MerkleHash.concatenate(
                    this.leftNode.hash.getValue(), this.rightNode.hash.getValue()));
        }

        if (this.parent != null) {
            this.parent.computeHash();
        }
    }

}
