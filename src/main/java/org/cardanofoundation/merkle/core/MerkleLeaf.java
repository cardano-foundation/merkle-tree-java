package org.cardanofoundation.merkle.core;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Constr
public class MerkleLeaf implements MerkleTree {

    @PlutusField
    private byte[] hash;

}
