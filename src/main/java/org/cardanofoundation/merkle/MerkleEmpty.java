package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Constr(alternative = 0)
public class MerkleEmpty<T> implements MerkleElement<T> {

  public static <T> MerkleEmpty<T> create() {
    return new MerkleEmpty<T>();
  }
}
