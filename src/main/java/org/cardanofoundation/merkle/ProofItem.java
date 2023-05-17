package org.cardanofoundation.merkle;

import com.bloxbean.cardano.client.plutus.annotation.Constr;
import java.util.HexFormat;

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
  class Left implements ProofItem {

    private final byte[] hash;

    public Left(byte[] hash) {
      this.hash = hash;
    }

    public byte[] getHash() {
      return hash;
    }

    @Override
    public String toString() {
      return "Left{" + "hash=0x" + HexFormat.of().formatHex(hash) + '}';
    }
  }

  @Constr(alternative = 1)
  class Right implements ProofItem {
    private final byte[] hash;

    public Right(byte[] hash) {

      this.hash = hash;
    }

    public byte[] getHash() {
      return hash;
    }

    @Override
    public String toString() {
      return "Left{" + "hash=" + HexFormat.of().formatHex(hash) + '}';
    }
  }
}
