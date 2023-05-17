package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import java.util.HexFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Constr(alternative = 1)
public class MerkleLeaf<T> implements MerkleElement<T> {

  @PlutusField private final T item;

  @PlutusField private final byte[] itemHash;

  @Override
  public String toString() {
    return "MerkleLeaf{"
        + "item="
        + item
        + ", itemHash=0x"
        + HexFormat.of().formatHex(itemHash)
        + '}';
  }
}
