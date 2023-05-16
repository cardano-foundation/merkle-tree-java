package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import lombok.ToString;

public interface ProofItem {

  default byte[] hash() {
    if (this instanceof Left l) {
      return l.getHash();
    }
    if (this instanceof Right r) {
      return r.getHash();
    }

    throw new IllegalStateException("Unexpected value.");
  }

  @Constr(alternative = 0)
  @ToString
  class Left implements ProofItem {

    private byte[] hash;

    public Left(byte[] hash) {
      this.hash = hash;
    }

    public byte[] getHash() {
      return hash;
    }
  }

  @Constr(alternative = 1)
  @ToString
  class Right implements ProofItem {
    private byte[] hash;

    public Right(byte[] hash) {

      this.hash = hash;
    }

    public byte[] getHash() {
      return hash;
    }
  }
}
