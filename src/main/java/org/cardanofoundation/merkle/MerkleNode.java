package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@Constr(alternative = 2)
public class MerkleNode<T> implements MerkleElement<T> {

  @PlutusField private byte[] hash;

  @PlutusField private MerkleElement<T> left;

  @PlutusField private MerkleElement<T> right;
}
