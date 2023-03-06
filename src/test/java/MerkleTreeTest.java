import lombok.val;
import org.cardanofoundation.merkle.util.Hashing;
import org.cardanofoundation.merkle.MerkleTree;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MerkleTreeTest {

    @Test
    public void testTree1() {
        val tree = new MerkleTree();

        tree.appendLeaf(Hashing.sha2_256("dog"));
        tree.appendLeaf(Hashing.sha2_256("cat"));
        tree.appendLeaf(Hashing.sha2_256("mouse"));
        tree.appendLeaf(Hashing.sha2_256("horse"));

        val root = tree.buildTree();

        val rootHash = HexFormat.of().formatHex(root);

        assertEquals("bd80e6bec9c2ef6158cf6a74f7f87531e94e0a824b9ba6db28c9a00ba418d452", rootHash);
    }

    @Test
    public void testTree2() {
        val tree = new MerkleTree();

        tree.appendLeaf(Hashing.sha2_256("dog"));
        tree.appendLeaf(Hashing.sha2_256("cat"));
        tree.appendLeaf(Hashing.sha2_256("mouse"));
        tree.appendLeaf(Hashing.sha2_256("horse"));
        tree.appendLeaf(Hashing.sha2_256("elephant"));

        val root = tree.buildTree();

        val rootHash = HexFormat.of().formatHex(root);

        assertEquals("42d71045cb757da525712753c5a757985bdf6e94ed0c225f08ef79cc9e6f26b8", rootHash);
    }

    @Test
    public void testTree3() {
        val tree = new MerkleTree();

        tree.appendLeaf(Hashing.sha2_256("dog"));
        tree.appendLeaf(Hashing.sha2_256("cat"));
        tree.appendLeaf(Hashing.sha2_256("mouse"));
        tree.appendLeaf(Hashing.sha2_256("horse"));

        tree.appendLeaf(Hashing.sha2_256("elephant"));
        tree.appendLeaf(Hashing.sha2_256("wolf"));
        tree.appendLeaf(Hashing.sha2_256("gopher"));
        tree.appendLeaf(Hashing.sha2_256("squirrel"));
        tree.appendLeaf(Hashing.sha2_256("badger"));
        tree.appendLeaf(Hashing.sha2_256("bobcat"));
        tree.appendLeaf(Hashing.sha2_256("owl"));
        tree.appendLeaf(Hashing.sha2_256("bird"));

        val root = tree.buildTree();

        val rootHash = HexFormat.of().formatHex(root);

        assertEquals("17d097548161ff8afa8b6d6f3c6e38d938a0381c9941f5abdeb3a7810b904e01", rootHash);
    }

}
