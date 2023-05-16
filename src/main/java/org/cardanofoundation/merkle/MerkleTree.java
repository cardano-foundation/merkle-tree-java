package org.cardanofoundation.merkle;

import static org.cardanofoundation.util.Hashing.combineHash;
import static org.cardanofoundation.util.Hashing.sha2_256;
import static org.cardanofoundation.util.Optionals.findFirst;

import io.vavr.collection.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import lombok.val;
import org.cardanofoundation.util.Hashing;

/** Sha256_2 backed merkle tree */
public class MerkleTree<T> {

  public static <T> MerkleElement<T> createFromItems(
      List<T> items, Function<T, byte[]> serialiserFn) {
    return doFrom(items, serialiserFn, items.size());
  }

  // TODO this can look nicer when JDK supports pattern matching on lists
  private static <T> MerkleElement<T> doFrom(
      List<T> items, Function<T, byte[]> serialiserFn, int len) {
    if (items.isEmpty()) {
      return MerkleEmpty.create();
    }
    if (items.size() == 1) {
      val item = items.head();
      val serialised = serialiserFn.apply(item);

      return new MerkleLeaf<T>(item, sha2_256(serialised));
    }

    val cutOff = len / 2;
    val left = doFrom(items.subSequence(0, cutOff), serialiserFn, cutOff);
    val right = doFrom(items.subSequence(cutOff, items.size()), serialiserFn, (len - cutOff));

    val hash = Hashing.combineHash(left.itemHash(), right.itemHash());

    return new MerkleNode<T>(hash, left, right);
  }

  public static <T> Optional<List<ProofItem>> getProof(
      MerkleElement<T> root, T item, Function<T, byte[]> serialiserFn) {
    return doGetProof(root, sha2_256(serialiserFn.apply(item)), List.empty());
  }

  private static <T> Optional<List<ProofItem>> doGetProof(
      MerkleElement<T> root, byte[] itemHash, List<ProofItem> proof) {
    if (root.isEmpty()) {
      return Optional.empty();
    }

    if (root instanceof MerkleLeaf<T> ml) {
      if (Arrays.equals(ml.getItemHash(), itemHash)) {
        return Optional.of(proof);
      }

      return Optional.empty();
    }

    if (root instanceof MerkleNode<T> mn) {
      val goLeft =
          doGetProof(
              mn.getLeft(), itemHash, proof.prepend(new ProofItem.Right(mn.getRight().itemHash())));
      val goRight =
          doGetProof(
              mn.getRight(), itemHash, proof.prepend(new ProofItem.Left(mn.getLeft().itemHash())));

      return findFirst(List.of(goLeft, goRight));
    }

    throw new IllegalStateException("Unexpected value.");
  }

  public static <T> boolean verifyProof(
      byte[] rootHash, // merkle root hash
      T item,
      List<ProofItem> proof,
      Function<T, byte[]> serialiserFn) {
    val serialisedItem = serialiserFn.apply(item);

    return doVerifyProof(sha2_256(serialisedItem), rootHash, proof);
  }

  private static boolean doVerifyProof(byte[] itemHash, byte[] elementHash, List<ProofItem> proof) {
    if (proof.size() == 0) {
      return Arrays.equals(itemHash, elementHash);
    }

    val head = proof.head();

    if (head instanceof ProofItem.Left l) {
      return doVerifyProof(combineHash(l.getHash(), itemHash), elementHash, proof.tail());
    }
    if (head instanceof ProofItem.Right r) {
      return doVerifyProof(combineHash(itemHash, r.getHash()), elementHash, proof.tail());
    }

    throw new IllegalStateException("Unexpected value.");
  }
}
