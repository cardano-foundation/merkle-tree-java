package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

@Getter
@Constr(alternative = 2)
public class MerkleTree {

    @PlutusField
    private MerkleNode root;

    @PlutusField
    private List<MerkleNode> nodes;

    @PlutusField
    private List<MerkleNode> leaves;

    public MerkleTree() {
        this.nodes = new ArrayList<>();
        this.leaves = new ArrayList<>();
    }

    public MerkleNode appendLeaf(MerkleNode node) {
        this.nodes.add(node);
        this.leaves.add(node);
        return node;
    }

    public void appendLeaves(MerkleNode[] nodes) {
        for (MerkleNode node : nodes) {
            this.appendLeaf(node);
        }
    }

    public MerkleNode appendLeaf(byte[] hash) {
        return this.appendLeaf(new MerkleNode(hash));
    }

    public byte[] addTree(MerkleTree tree) {
        tree.leaves.forEach(this::appendLeaf);

        return this.buildTree();
    }

    public byte[] buildTree() {
        this.buildTree(this.leaves);

        return this.root.getHash();
    }

    public void buildTree(List<MerkleNode> nodes) {
        if (nodes.size() == 1) {
            this.root = nodes.get(0);
        } else {
            List<MerkleNode> parents = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i += 2) {
                val right = (i + 1 < nodes.size()) ? nodes.get(i + 1) : null;
                val parent = new MerkleNode(nodes.get(i), right);
                parents.add(parent);
            }
            buildTree(parents);
        }
    }

    private MerkleNode findLeaf(byte[] hash) {
        return this.leaves.stream()
                .filter((leaf) -> leaf.getHash() == hash)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        if (root != null && root.getHash() != null ) {
            return "MerkleTree{" +
                    "root=" + HexFormat.of().formatHex(root.getHash()) +
                    '}';
        }

        return "MerkleTree<EMPTY>";
    }

}
