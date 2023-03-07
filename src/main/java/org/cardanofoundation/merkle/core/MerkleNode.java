package org.cardanofoundation.merkle.core;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@Constr(alternative = 2)
public class MerkleNode implements MerkleTree {

    @PlutusField
    private byte[] hash;

    @PlutusField
    private MerkleTree left;

    @PlutusField
    private MerkleTree right;

}
