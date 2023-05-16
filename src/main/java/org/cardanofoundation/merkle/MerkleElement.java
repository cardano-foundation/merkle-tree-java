package org.cardanofoundation.merkle;

public interface MerkleElement<T> {

  default boolean isEmpty() {
    return this instanceof MerkleEmpty;
  }

  default byte[] itemHash() {
    if (this instanceof MerkleEmpty) {
      return new byte[] {};
    }

    if (this instanceof MerkleLeaf<T> ml) {
      return ml.getItemHash();
    }
    if (this instanceof MerkleNode<T> mn) {
      return mn.getHash();
    }

    throw new IllegalStateException("Unexpected value!");
  }

  default T item() {
    if (this instanceof MerkleEmpty) {
      return null;
    }

    if (this instanceof MerkleLeaf<T> ml) {
      return ml.item();
    }

    throw new IllegalStateException("Unexpected value!");
  }

  default int size() {
    if (this instanceof MerkleEmpty) {
      return 0;
    }

    if (this instanceof MerkleLeaf<?> ml) {
      return 1;
    }

    if (this instanceof MerkleNode<T> mn) {
      return mn.getLeft().size() + mn.getRight().size();
    }

    throw new IllegalStateException("Unexpected value!");
  }
}
