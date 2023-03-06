package org.cardanofoundation.merkle.core;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import lombok.AllArgsConstructor;

@Constr
@AllArgsConstructor
public class MerkleEmpty implements MerkleTree {

    public final static MerkleEmpty EMPTY = new MerkleEmpty();

}
