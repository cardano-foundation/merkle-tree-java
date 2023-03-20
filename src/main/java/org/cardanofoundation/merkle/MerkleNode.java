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
public class MerkleNode implements MerkleElement {

    @PlutusField
    private byte[] hash;

    @PlutusField
    private MerkleElement left;

    @PlutusField
    private MerkleElement right;

}
