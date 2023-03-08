# Merkle Tree implementation in Java / Aiken

This is an **incubating** project to implement Plutus compatible Merkle Tree implementation in java and compatible contract equivalent written in Aiken (https://aiken-lang.org/).

[![License](https://img.shields.io:/github/license/cardano-foundation/merkle-tree-java?label=license)](https://github.com/cardano-foundation/merkle-tree-java/blob/master/LICENSE)

## Requirements
- JDK17
- maven17
- Aiken

## Building
```
git clone https://github.com/cardano-foundation/merkle-tree-java
cd merkle-tree-java
mvn clean install
```

## Example
```
var items = List.of(
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
);
var mt = MerkleTree.createFromHashes(items);

var rootHash = HexFormat.of().formatHex(mt.rootHash());
System.out.println(rootHash);
// prints out
//fc84e654aa6f5ca9c72adab1ab2c157298fdefd658f65d7d2231009c4d763ef0
```

## TODO
- more unit tests
- java docs
- publish release to maven central 
- bloxbean library could have minimalistic library, something like: cardano-client-serialization.jar
