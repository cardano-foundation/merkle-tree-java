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

/**
 * Merkle Tree implementation using sha2_256 hashing function. The unique thing about this
 * implementation is that it is compatible with on-chain code written in Aiken.
 *
 * @param <T> - user defined type backing this Merkle Tree
 */
public class MerkleTree<T> {

  /**
   * Checks whether given merkle tree is empty or not
   *
   * @param root - Merkle Tree (root node)
   * @return - true if empty, false otherwise
   * @param <T> - user defined type backing this Merkle Tree
   */
  public static <T> boolean isEmpty(MerkleElement<T> root) {
    return root instanceof MerkleEmpty;
  }

  /**
   * Create a Merkle Tree from a list of elements (using vavr list).
   *
   * @param items - original items to construct merkle list from
   * @param serialiserFn - function to serialise a user defined item into a byte-array (should not
   *     apply sha2_256 hashing)
   * @return - Merkle Tree
   * @param <T> - user defined type backing this Merkle Tree
   */
  public static <T> MerkleElement<T> fromList(List<T> items, Function<T, byte[]> serialiserFn) {
    return doFromList(items, serialiserFn, items.size());
  }

  /**
   * Create a Merkle Tree from a list of elements (using java.util list).
   *
   * @param items - original items to construct merkle list from
   * @param serialiserFn - function to serialise a user defined item into a byte-array (should not
   *     apply sha2_256 hashing)
   * @return - Merkle Tree
   * @param <T> - user defined type backing this Merkle Tree
   */
  public static <T> MerkleElement<T> fromList(
      java.util.List<T> items, Function<T, byte[]> serialiserFn) {
    return doFromList(List.ofAll(items.stream()), serialiserFn, items.size());
  }

  private static <T> MerkleElement<T> doFromList(
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
    val left = doFromList(items.subSequence(0, cutOff), serialiserFn, cutOff);
    val right = doFromList(items.subSequence(cutOff, items.size()), serialiserFn, (len - cutOff));

    val hash = Hashing.combineHash(left.itemHash(), right.itemHash());

    return new MerkleNode<T>(hash, left, right);
  }

  /**
   * Get a proof for this Merkle Tree based on a provided user defined item.
   *
   * @param root - Merkle Tree (root node)
   * @param item - user defined item to be checked
   * @param serialiserFn - function to serialise a user defined item into a byte-array (should not
   *     apply sha2_256 hashing)
   * @return - Merkle Proof for this particular item
   * @param <T> - user defined type backing this Merkle Proof
   */
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

  /**
   * Verifies the provided proof against root hash of the tree.
   *
   * @param rootHash - Merkle Tree hash
   * @param item - item to be verified for presence
   * @param proof - previously generated Merkle Proof (notice that empty list is also a valid proof)
   * @param serialiserFn - function to serialise a user defined item into a byte-array (should not
   *     apply sha2_256 hashing)
   * @return <code>true</code> when a proof is valid for the given item, <code>false</code>
   *     otherwise
   * @param <T> - user defined type backing this list
   */
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

  /**
   * Returns the original items stored in the Merkle Tree as list.
   *
   * @param root - Merkle Root element
   * @return - list of original elements (before hashing)
   * @param <T> - user defined type backing this list
   */
  public static <T> List<T> toList(MerkleElement<T> root) {
    return doGetList(root, List.empty());
  }

  private static <T> List<T> doGetList(MerkleElement<T> element, List<T> acc) {
    if (element instanceof MerkleEmpty<?>) {
      return acc;
    }
    if (element instanceof MerkleNode<T> mn) {
      val goLeft = doGetList(mn.getLeft(), acc);
      val goRight = doGetList(mn.getRight(), acc);

      return goLeft.appendAll(goRight);
    }

    if (element instanceof MerkleLeaf<T> ml) {
      return acc.append(ml.getItem());
    }

    throw new IllegalStateException("Unexpected value.");
  }

  /**
   * Adds new item to a Merkle Tree.
   *
   * <p>Caution: method is inefficient (works well on small data sets but performs poorly for > 1
   * mln leafs Merkle Tree)
   *
   * @param root - Merkle Tree root
   * @param item - item to be added
   * @param serialiserFn - function to serialise a user defined item into a byte-array (should not
   *     apply sha2_256 hashing)
   * @return new Merkle Tree root
   * @param <T> - user defined type backing this list
   */
  public static <T> MerkleElement<T> add(
      MerkleElement<T> root, T item, Function<T, byte[]> serialiserFn) {
    val items = MerkleTree.toList(root);

    return fromList(items.append(item), serialiserFn);
  }

  /**
   * Removes an item from a Merkle Tree.
   *
   * <p>Caution: method is inefficient (works well on small data sets but performs poorly for > 1
   * mln leafs Merkle Tree)
   *
   * @param root - Merkle Tree root
   * @param item - item to be removed
   * @param serialiserFn - function to serialise a user defined item into a byte-array (should not
   *     apply sha2_256 hashing)
   * @return new Merkle Tree root
   * @param <T> - user defined type backing this list
   */
  public static <T> MerkleElement<T> remove(
      MerkleElement<T> root, T item, Function<T, byte[]> serialiserFn) {
    val items = MerkleTree.toList(root);

    return fromList(items.remove(item), serialiserFn);
  }
}
