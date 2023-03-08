# Merkle Tree implementation in Java / Aiken

This is an **incubating** project to implement Plutus compatible Merkle Tree implementation in java and compatible contract equivalent written in Aiken (https://aiken-lang.org/).

[![License](https://img.shields.io:/github/license/cardano-foundation/merkle-tree-java?label=license)](https://github.com/cardano-foundation/merkle-tree-java/blob/master/LICENSE)

## Requirements
- JDK17
- maven17
- Aiken

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
