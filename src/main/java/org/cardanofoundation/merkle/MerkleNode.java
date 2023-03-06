package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.cardanofoundation.merkle.util.Bytes;
import org.cardanofoundation.merkle.util.Hashing;

import javax.annotation.Nullable;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Constr
public class MerkleNode {

    @PlutusField
    private byte[] hash;

    @PlutusField
    @Nullable
    private MerkleNode leftNode;

    @PlutusField
    @Nullable
    private MerkleNode rightNode;

    @PlutusField
    @Nullable
    private MerkleNode parent;

    public MerkleNode(byte[] hash) {
        if (hash == null) throw new NullPointerException("hash cannot be null");
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
            this.hash = Hashing.sha2_256(Bytes.concat(
                    this.leftNode.hash, this.rightNode.hash));
        }

        if (this.parent != null) {
            this.parent.computeHash();
        }
    }

}
