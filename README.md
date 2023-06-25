# Merkle Tree implementation in Java / Aiken

This is an **incubating** project to implement Plutus compatible Merkle Tree implementation in java and compatible contract equivalent written in Aiken (https://aiken-lang.org/).

[![Build](https://github.com/cardano-foundation/merkle-tree-java/actions/workflows/build.yml/badge.svg)](https://github.com/cardano-foundation/merkle-tree-java/actions/workflows/build.yml)
[![License](https://img.shields.io:/github/license/cardano-foundation/merkle-tree-java?label=license)](https://github.com/cardano-foundation/merkle-tree-java/blob/master/LICENSE)
[![Discord](https://img.shields.io/discord/1022471509173882950)](https://discord.gg/4WVNHgQ7bP)

## Motivation
Merkle Trees are a cryptographic commitment scheme based on hashes. This particular implementation uses sha2_256 algorithm.
Merkle Trees are useful when we need to attest validity of something based on having only a small proof as well as cases
where we need to check whether dataset has not been tampered with (via root merkle hash verification).

In this project, offchain part of the Merkle Tree is written in Java and on-chain part is written in Aiken (https://aiken-lang.org).
If you are looking for Plutus / Haskell implementation of Merkle Tree we recommend to look at: Hydra's implementation,
which can be accessed at: https://github.com/input-output-hk/hydra/blame/master/plutus-merkle-tree/src/Plutus/MerkleTree.hs

Please note that various Merkle Tree implementations can produce differently looking trees which can have
different merkle node and root hashes, this implementation's goal is to have on-chain contract and off-chain part
in exact synchrony. Needless to say, insertion order of elements into the tree plays
a role in the final merkle root hash as well as intermediate elements (nodes).

## Requirements
- JDK (>= 17)
- maven (>= 3)
- Aiken (>= 1.0.5-alpha)

## Cloning
git clone https://github.com/cardano-foundation/merkle-tree-java

## Java Building
Java MerkleTree is located in `src/main/java`
```
cd merkle-tree-java
mvn clean install
```

## Contracts Building
Cardano's Aiken on chain contracts are located in `contracts` directory.

```
cd aiken
aiken check
```

Aiken check should invoke all tests. Since Aiken doesn't support libraries which depend on std lib, typically for now
you have to copy and paste Aiken code into your smart contract / Cardano validator project.

Notice that trying to invoke:
```
aiken build
```
command will give a warning with the following message:
```
⚠ You do not have any validators to build!
```

because there are no on-chain validators in this contract. Even though plutus.json will be generated, it will not have
compiled onchain code and endpoint definitions.

## Example
```
  MerkleElement<String> mt = MerkleTree.fromList(List.of("dog", "cat", "mouse", "horse"), fromStringFun());
  byte[] rootHash = mt.itemHash();
  System.out.println("Root Hash:" + HexFormat.of().formatHex(rootHash));


  String item = "horse";

  Optional<List<ProofItem>> proof = MerkleTree.getProof(mt, item, fromStringFun());

  System.out.println("Proof:" + proof);

  boolean isValid = MerkleTree.verifyProof(rootHash, item, proof.orElseThrow(), fromStringFun());

  System.out.println("IsValidProof:" + isValid);
```
prints:
```
Root Hash: bd80e6bec9c2ef6158cf6a74f7f87531e94e0a824b9ba6db28c9a00ba418d452
Proof: Optional[List(Left{hash=0x47c5c28cae2574cdf5a194fe7717de68f8276f4bf83e653830925056aeb32a48}, Left{hash=0xd08508c86526cfde6c822b1b841f6d2615af61c94e910b0aeb0aa81d193f4ab5})]
IsValidProof: true
```
