package org.cardanofoundation;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.vavr.collection.List;
import java.util.Optional;
import java.util.function.Function;
import org.cardanofoundation.merkle.MerkleElement;
import org.cardanofoundation.merkle.MerkleTree;
import org.cardanofoundation.merkle.ProofItem;

public class Example {

  public static void main(String[] args) {
    MerkleElement<String> mt =
        MerkleTree.fromList(List.of("dog", "cat", "mouse", "horse"), fromStringFun());
    System.out.println("Root:" + mt);

    String item = "horse";

    Optional<List<ProofItem>> proof = MerkleTree.getProof(mt, item, fromStringFun());

    System.out.println("Proof:" + proof);

    boolean isValid =
        MerkleTree.verifyProof(mt.itemHash(), item, proof.orElseThrow(), fromStringFun());

    System.out.println("IsValidProof:" + isValid);
  }

  private static Function<String, byte[]> fromStringFun() {
    return str -> str.getBytes(UTF_8);
  }
}
