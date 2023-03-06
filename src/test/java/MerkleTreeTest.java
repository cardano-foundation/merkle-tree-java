import lombok.val;
import org.cardanofoundation.merkle.core.MerkleLeaf;
import org.cardanofoundation.merkle.core.MerkleTreeBuilder;
import org.cardanofoundation.merkle.util.Hashing;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MerkleTreeTest {

    @Test
    public void testRootHash1() {
        val mt = new MerkleLeaf(Hashing.sha2_256("dog"));

        assertEquals("cd6357efdd966de8c0cb2f876cc89ec74ce35f0968e11743987084bd42fb8944", HexFormat.of().formatHex(mt.rootHash()));
    }

    @Test
    public void testTree1() {
        val mt = MerkleTreeBuilder.createFromItems(List.of(
                "dog",
                "cat",
                "mouse",
                "horse"
        ), item -> Hashing.sha2_256(item.getBytes(StandardCharsets.UTF_8)));

        val rootHash = HexFormat.of().formatHex(mt.rootHash());

        assertEquals("bd80e6bec9c2ef6158cf6a74f7f87531e94e0a824b9ba6db28c9a00ba418d452", rootHash);
    }

    @Test
    public void testTree2() {
        val mt = MerkleTreeBuilder.createFromHashes(List.of(
                Hashing.sha2_256("dog"),
                Hashing.sha2_256("cat"),
                Hashing.sha2_256("mouse"),
                Hashing.sha2_256("horse"),
                Hashing.sha2_256("elephant")
        ));

        val rootHash = HexFormat.of().formatHex(mt.rootHash());

        assertEquals("00e3bd2c9df3e934f51d8bc96b6d985aaab5bf1624a7fcdb90a8eba592417531", rootHash);

//        // 71d7b98d321250239376b794e9a92afb347d15175274be5a017fcf316dc3a23a
//        assertEquals("42d71045cb757da525712753c5a757985bdf6e94ed0c225f08ef79cc9e6f26b8", rootHash);
    }

    @Test
    public void testTree3() {
        val items = List.of(
                Hashing.sha2_256("dog"),
                Hashing.sha2_256("cat"),
                Hashing.sha2_256("mouse"),
                Hashing.sha2_256("horse"),
                Hashing.sha2_256("elephant"),
                Hashing.sha2_256("wolf"),
                Hashing.sha2_256("gopher"),
                Hashing.sha2_256("squirrel"),
                Hashing.sha2_256("badger"),
                Hashing.sha2_256("bobcat"),
                Hashing.sha2_256("owl"),
                Hashing.sha2_256("bird")
        );
        val mt = MerkleTreeBuilder.createFromHashes(items);

        val rootHash = HexFormat.of().formatHex(mt.rootHash());

        assertEquals("fc84e654aa6f5ca9c72adab1ab2c157298fdefd658f65d7d2231009c4d763ef0", rootHash);
        assertEquals(items.size(), mt.size());

//        // 102bd9546ccaca971a3929212fc9868dc2af5bbd5435610fa2d9a359340bd6a8
//        assertEquals("17d097548161ff8afa8b6d6f3c6e38d938a0381c9941f5abdeb3a7810b904e01", rootHash);
    }

}
