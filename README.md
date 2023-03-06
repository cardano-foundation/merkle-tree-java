# Merkle Tree implementation in Java

This is **incubating** project to implement Plutus compatible Merkle Tree implementation in java. 

[![License](https://img.shields.io:/github/license/cardano-foundation/hydra-java-client?label=license)](https://github.com/cardano-foundation/hydra-java-client/blob/master/LICENSE)

## Requirements
- JDK17
- maven17 

## Building
```
git clone https://github.com/cardano-foundation/merkle-tree-java
cd merkle-tree-java
mvn clean install
```

## Example
```
val mt = MerkleTreeBuilder.createFromHashes(List.of(
        Hashing.sha2_256("dog"),
        Hashing.sha2_256("cat"),
        Hashing.sha2_256("mouse"),
        Hashing.sha2_256("horse"),
        Hashing.sha2_256("elephant"),
        Hashing.sha2_256("wolf"),
        Hashing.sha2_256("gopher"),
        Hashing.sha2_256("squirrel"),
        Hashing.sha2_256("badger"),
        Hashing.sha2_256("bobcat"),
        Hashing.sha2_256("owl"),
        Hashing.sha2_256("bird")
));

val rootHash = HexFormat.of().formatHex(mt.rootHash());

assertEquals("fc84e654aa6f5ca9c72adab1ab2c157298fdefd658f65d7d2231009c4d763ef0", rootHash);
```

## TODO
- more unit tests
- java docs
- publish to maven central 
- another maven module doing serialisation?
- merkle proof handling
- bloxbean library should have minimalistic interface for PlutusDataSerializer (change in bloxbean lib)
