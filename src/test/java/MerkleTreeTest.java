import lombok.val;
import org.cardanofoundation.merkle.MerkleHash;
import org.cardanofoundation.merkle.MerkleTree;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MerkleTreeTest {

    @Test
    public void testTree() {
        val tree = new MerkleTree();

        System.out.println(tree);

        tree.appendLeaf(MerkleHash.create("dog"));
        tree.appendLeaf(MerkleHash.create("cat"));
        tree.appendLeaf(MerkleHash.create("mouse"));
        tree.appendLeaf(MerkleHash.create("horse"));

        val root = tree.buildTree();
        System.out.println(root);

        System.out.println(tree);

        assertEquals("bd80e6bec9c2ef6158cf6a74f7f87531e94e0a824b9ba6db28c9a00ba418d452", HexFormat.of().formatHex(tree.getRoot().getHash().getValue()));
    }

}
