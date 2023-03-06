import lombok.val;
import org.cardanofoundation.merkle.util.Hashing;
import org.cardanofoundation.merkle.MerkleTree;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MerkleTreeTest {

    @Test
    public void testTree() {
        val tree = new MerkleTree();

        tree.appendLeaf(Hashing.sha2_256("dog"));
        tree.appendLeaf(Hashing.sha2_256("cat"));
        tree.appendLeaf(Hashing.sha2_256("mouse"));
        tree.appendLeaf(Hashing.sha2_256("horse"));

        val root = tree.buildTree();

        val rootHash = HexFormat.of().formatHex(root);

        assertEquals("bd80e6bec9c2ef6158cf6a74f7f87531e94e0a824b9ba6db28c9a00ba418d452", rootHash);
    }

}
