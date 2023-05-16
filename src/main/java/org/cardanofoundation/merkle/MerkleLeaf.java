package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Constr(alternative = 1)
public class MerkleLeaf<T> implements MerkleElement<T> {

  @PlutusField private T item;

  @PlutusField private byte[] itemHash;
}
