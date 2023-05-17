package org.cardanofoundation.merkle;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

import io.vavr.collection.List;
import java.util.*;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.cardanofoundation.util.Hashing;
import org.junit.jupiter.api.Test;

@Slf4j
public class MerkleTreeTest {

  @Test
  public void testCreateEmptyTree1() {
    val mt = MerkleTree.fromList(List.of(), fromStringFun());

    assertEquals("", HexFormat.of().formatHex(mt.itemHash()));
  }

  @Test
  public void testCreateEmptyTree2() {
    val mt = MerkleTree.fromList(List.of(), fromStringFun());

    assertTrue(mt instanceof MerkleEmpty);
  }

  @Test
  public void testMerkleEmpty() {
    val mt = new MerkleEmpty<>();

    assertEquals("", HexFormat.of().formatHex(mt.itemHash()));
  }

  @Test
  public void testMerkleLeaf() {
    val item = "dog";
    val ml = new MerkleLeaf<>(item, fromStringFun().andThen(Hashing::sha2_256).apply(item));

    assertEquals(
        "cd6357efdd966de8c0cb2f876cc89ec74ce35f0968e11743987084bd42fb8944",
        HexFormat.of().formatHex(ml.itemHash()));
  }

  @Test
  public void testMerkleTreeFromList1() {
    val mt = MerkleTree.fromList(List.of("dog", "cat", "mouse", "horse"), fromStringFun());

    val rootHash = HexFormat.of().formatHex(mt.itemHash());

    assertEquals("bd80e6bec9c2ef6158cf6a74f7f87531e94e0a824b9ba6db28c9a00ba418d452", rootHash);
  }

  @Test
  public void testMerkleTreeFromList2() {
    val mt =
        MerkleTree.fromList(
            List.of(
                "dog",
                "cat",
                "mouse",
                "horse",
                "elephant",
                "wolf",
                "gopher",
                "squirrel",
                "badger",
                "bobcat",
                "owl",
                "bird"),
            fromStringFun());

    val rootHash = HexFormat.of().formatHex(mt.itemHash());

    assertEquals("fc84e654aa6f5ca9c72adab1ab2c157298fdefd658f65d7d2231009c4d763ef0", rootHash);
  }

  @Test
  public void testMerkleTreeFromList3() {
    val items = new HashSet<String>();

    for (int i = 0; i < 1_000_000; i++) {
      items.add(UUID.randomUUID().toString());
    }

    val startTime = System.currentTimeMillis();

    val mt = MerkleTree.fromList(List.ofAll(items), fromStringFun());

    val endTime = System.currentTimeMillis();

    val time = (endTime - startTime);

    log.info("That took " + time + " milliseconds on average.");

    assertEquals(items.size(), mt.size());
  }

  @Test
  public void testTreeAdd1() {
    val orgItems =
        List.of(
            "dog",
            "cat",
            "mouse",
            "horse",
            "elephant",
            "wolf",
            "gopher",
            "squirrel",
            "badger",
            "bobcat",
            "owl",
            "bird"
        );

    val mt = MerkleTree.fromList(orgItems, fromStringFun());

    assertEquals(
        "fc84e654aa6f5ca9c72adab1ab2c157298fdefd658f65d7d2231009c4d763ef0",
        HexFormat.of().formatHex(mt.itemHash()));

    val newRoot = MerkleTree.add(mt, "beaver", fromStringFun());

    assertEquals(
        "b3e09c8895e5b1c0cc3e793d830693f218b8488041c79b7f5d2afc36bad70adb",
        HexFormat.of().formatHex(newRoot.itemHash()));
  }

  @Test
  public void testTreeAdd2() {
    val orgItems = new ArrayList<String>();

    for (int i = 0; i < 1_000_000; i++) {
      orgItems.add(UUID.randomUUID().toString());
    }

    val mt = MerkleTree.fromList(List.ofAll(orgItems), fromStringFun());

    val startTime = System.currentTimeMillis();

    val newRoot = MerkleTree.add(mt, UUID.randomUUID().toString(), fromStringFun());

    val endTime = System.currentTimeMillis();

    System.out.println("Time to add item: " + (endTime - startTime) + " ms");

    assertEquals(20_001, MerkleTree.toList(newRoot).size());
  }

  @Test
  public void testTreeRemove1() {
    val orgItems =
        List.of(
            "dog",
            "cat",
            "mouse",
            "horse",
            "elephant",
            "wolf",
            "gopher",
            "squirrel",
            "badger",
            "bobcat",
            "owl",
            "bird");

    val mt = MerkleTree.fromList(orgItems, fromStringFun());

    assertEquals(
        "fc84e654aa6f5ca9c72adab1ab2c157298fdefd658f65d7d2231009c4d763ef0",
        HexFormat.of().formatHex(mt.itemHash()));

    val newRoot = MerkleTree.remove(mt, "squirrel", fromStringFun());

    assertEquals(
        "a0c289d6c072d83703aeac30c0d99d513dad04a8225381939d8471189b8a522b",
        HexFormat.of().formatHex(newRoot.itemHash()));
    assertEquals(11, MerkleTree.toList(newRoot).size());
  }

  @Test
  public void testMerkleProof1() {
    val mt = MerkleTree.fromList(List.of("dog", "cat", "mouse"), fromStringFun());

    val item = "mouse";

    val proof = MerkleTree.getProof(mt, item, fromStringFun());

    assertEquals(2, proof.orElseThrow().size());
    val hash1 = proof.orElseThrow().get(0).hash();
    val hash2 = proof.orElseThrow().get(1).hash();

    assertEquals(
        "77af778b51abd4a3c51c5ddd97204a9c3ae614ebccb75a606c3b6865aed6744e",
        HexFormat.of().formatHex(hash1));
    assertEquals(
        "cd6357efdd966de8c0cb2f876cc89ec74ce35f0968e11743987084bd42fb8944",
        HexFormat.of().formatHex(hash2));
  }

  @Test
  public void testMerkleProof2() {
    val mt = MerkleTree.fromList(List.of("dog", "cat", "mouse"), fromStringFun());

    val item = "mouse";

    val proof = MerkleTree.getProof(mt, item, fromStringFun());

    assertTrue(proof.isPresent());

    val root = mt.itemHash();

    assertTrue(MerkleTree.verifyProof(root, item, proof.orElseThrow(), fromStringFun()));
  }

  @Test
  public void testMerkleProof3() {
    val mt = MerkleTree.fromList(List.of("dog"), fromStringFun());

    val item = "dog";

    val proof = MerkleTree.getProof(mt, item, fromStringFun());

    assertTrue(proof.isPresent());

    val root = mt.itemHash();

    assertTrue(MerkleTree.verifyProof(root, item, proof.orElseThrow(), fromStringFun()));
  }

  @Test
  public void testMerkleProof4() {
    val mt = MerkleTree.fromList(List.of("dog", "cat", "mouse", "horse"), fromStringFun());

    val item = "horse";

    val proof = MerkleTree.getProof(mt, item, fromStringFun());

    assertTrue(proof.isPresent());

    val root = mt.itemHash();

    assertTrue(MerkleTree.verifyProof(root, item, proof.orElseThrow(), fromStringFun()));
  }

  @Test
  public void testMerkleProof5() {
    val mt =
        MerkleTree.fromList(
            List.of("dog", "cat", "mouse", "horse", "pig", "bull"), fromStringFun());

    val item = "pig";

    val proof = MerkleTree.getProof(mt, item, fromStringFun());

    assertTrue(proof.isPresent(), "proof is available.");

    val root = mt.itemHash();

    assertTrue(
        MerkleTree.verifyProof(root, item, proof.orElseThrow(), fromStringFun()),
        "proof verification should match.");
  }

  @Test
  public void testMerkleToList1() {
    val originalList = List.of("dog", "cat", "mouse", "horse", "pig", "bull", "beaver");

    val mt = MerkleTree.fromList(originalList, fromStringFun());

    val items = MerkleTree.toList(mt);

    assertArrayEquals(items.toJavaArray(), originalList.toJavaArray());
  }

  @Test
  public void testMerkleToList2() {
    val originalList = new ArrayList<String>();

    for (int i = 0; i < 1_000_000; i++) {
      originalList.add(UUID.randomUUID().toString());
    }

    val mt = MerkleTree.fromList(List.ofAll(originalList), fromStringFun());

    val items = MerkleTree.toList(mt);

    assertArrayEquals(items.toJavaArray(), originalList.toArray());
  }

  @Test
  public void testMerkleProof6() {
    java.util.List<String> items = new java.util.ArrayList<>();

    for (int i = 0; i < 1_000_000; i++) {
      items.add(UUID.randomUUID().toString());
    }

    var item = randomItem(items).orElseThrow();

    val startTime = System.currentTimeMillis();

    MerkleElement<String> mt = MerkleTree.fromList(List.ofAll(items), fromStringFun());

    val proof = MerkleTree.getProof(mt, item, fromStringFun());
    val rootHash = mt.itemHash();

    assertTrue(proof.isPresent(), "proof should be available.");

    assertTrue(
        MerkleTree.verifyProof(rootHash, item, proof.orElseThrow(), fromStringFun()),
        "Proof verification should match.");

    val endTime = System.currentTimeMillis();

    val time = (endTime - startTime);

    log.info("That took " + time + " milliseconds on average.");

    assertEquals(items.size(), mt.size());
  }

  private static Function<String, byte[]> fromStringFun() {
    return str -> str.getBytes(UTF_8);
  }

  private static <E> Optional<E> randomItem(Collection<E> e) {

    return e.stream().skip((int) (e.size() * Math.random())).findFirst();
  }
}
