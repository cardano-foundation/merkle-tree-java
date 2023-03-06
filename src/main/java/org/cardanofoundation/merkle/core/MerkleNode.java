package org.cardanofoundation.merkle.core;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import com.bloxbean.cardano.client.plutus.annotation.PlutusField;
import lombok.*;

import javax.annotation.Nullable;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@Constr
public class MerkleNode implements MerkleTree {

    @PlutusField
    private byte[] hash;

    @PlutusField
    @Nullable
    private MerkleTree left;

    @PlutusField
    @Nullable
    private MerkleTree right;

}