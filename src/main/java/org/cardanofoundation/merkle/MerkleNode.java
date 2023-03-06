package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Constr
public class MerkleNode {

    @PlutusField
    private byte[] hash;

    @PlutusField
    private MerkleNode leftNode;

    @PlutusField
    private MerkleNode rightNode;

    @PlutusField
    private MerkleNode parent;

    public MerkleNode() {
    }

    public MerkleNode(byte[] hash) {
        this.hash = hash;
    }

    public MerkleNode(MerkleNode left, MerkleNode right) {
        this.leftNode = left;
        this.rightNode = right;
        this.leftNode.parent = this;
        if (this.rightNode != null) {
            this.rightNode.parent = this;
        }

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
                    this.leftNode.hash, this.rightNode.hash)).getValue();
        }

        if (this.parent != null) {
            this.parent.computeHash();
        }
    }

}
