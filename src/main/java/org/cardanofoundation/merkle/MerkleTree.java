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

//    public List<MerkleProofHash> auditProof(MerkleHash leafHash) {
//        List<MerkleProofHash> auditTrail = new ArrayList<>();
//
//        val leafNode = this.findLeaf(leafHash);
//
//        if (leafNode != null) {
//            val parent = leafNode.getParent();
//            this.buildMerkleProof(auditTrail, parent, leafNode);
//        }
//
//        return auditTrail;
//    }

//    public static boolean verifyAudit(MerkleHash rootHash, MerkleHash leafHash, List<MerkleProofHash> auditTrail) {
//        MerkleHash testHash = leafHash;
//
//        for (var auditHash : auditTrail) {
//            testHash = auditHash.direction() == MerkleProofHash.MerkleProofItem.RIGHT
//                    ? MerkleHash.create(testHash, auditHash.hash())
//                    : MerkleHash.create(auditHash.hash(), testHash);
//        }
//
//        return Arrays.equals(testHash.getValue(), rootHash.getValue());
//    }

    private MerkleNode findLeaf(byte[] hash) {
        return this.leaves.stream()
                .filter((leaf) -> leaf.getHash() == hash)
                .findFirst()
                .orElse(null);
    }

//    private void buildMerkleProof(List<MerkleProofHash> merkleProof, MerkleNode parent, MerkleNode child) {
//        if (parent != null) {
//            val nextChild = parent.getLeftNode() == child ? parent.getRightNode() : parent.getLeftNode();
//            val direction = parent.getLeftNode() == child
//                    ? MerkleProofHash.MerkleProofItem.RIGHT
//                    : MerkleProofHash.MerkleProofItem.LEFT;
//
//            if (nextChild != null) {
//                merkleProof.add(new MerkleProofHash(nextChild.getHash(), direction));
//            }
//
//            this.buildMerkleProof(merkleProof, parent.getParent(), child.getParent());
//        }
//    }

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
