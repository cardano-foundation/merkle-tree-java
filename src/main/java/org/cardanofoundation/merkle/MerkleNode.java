package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import java.util.HexFormat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@Constr(alternative = 2)
public class MerkleNode<T> implements MerkleElement<T> {

  @PlutusField private final byte[] hash;

  @PlutusField private final MerkleElement<T> left;

  @PlutusField private final MerkleElement<T> right;

  @Override
  public String toString() {
    return "MerkleNode{" + "root_hash=0x" + HexFormat.of().formatHex(hash) + '}';
  }
}
