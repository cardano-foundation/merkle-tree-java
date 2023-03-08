# Merkle Tree implementation in Java / Aiken

This is an **incubating** project to implement Plutus compatible Merkle Tree implementation in java and compatible contract equivalent written in Aiken (https://aiken-lang.org/).

[![License](https://img.shields.io:/github/license/cardano-foundation/merkle-tree-java?label=license)](https://github.com/cardano-foundation/merkle-tree-java/blob/master/LICENSE)

## Why and what is this?
Merkle Trees are a cryptographic commitment scheme based on hashes. This particular implementation uses sha2_256 algorithm.
Merkle Trees are useful when we need to attest validity of something based on having only a small proof as well as cases
where we need to check presence and some dataset being tamper proof (via root merkle hash verification).

Classical Merkle Tree will contain both item and hash of the item in the merkle leafs. In order to minimize transaction sizes
and what goes on chain (space reasons), this implementation of Merkle Trees does not contain original item,
it contains only hashed version of the item. While technically this is no longer Merkle Tree, there is no alternative name
we are aware of. Needless to say, it is very easy to change this implementation to actually contain original item as well.

Please note that various Merkle Tree implementations can produce differently looking trees which can have
different merkle node and root hashes, this implementation's goal is to have on-chain contract and off-chain part
in exact synchrony.

In case it was not obvious, last but not least, notice that insertion order of elements into the tree plays
a role in the final merkle root hash as well as intermediate elements.

Offchain part of the Merkle Tree is written in Java and on-chain part is written in Aiken (https://aiken-lang.org).
If you are looking for Plutus / Haskell implementation of Merkle Tree we recommend to look at: Hydra's implementation,
which can be accessed at: https://github.com/input-output-hk/hydra/blame/master/plutus-merkle-tree/src/Plutus/MerkleTree.hs

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

var rootHash = HexFormat.of().formatHex(mt.elementHash());

assertEquals("fc84e654aa6f5ca9c72adab1ab2c157298fdefd658f65d7d2231009c4d763ef0", rootHash);
assertEquals(items.size(), mt.size());
System.out.println(rootHash);

// prints out
//fc84e654aa6f5ca9c72adab1ab2c157298fdefd658f65d7d2231009c4d763ef0
```

## TODO
- more unit tests
- java docs
- publish release to maven central 
- bloxbean library could have minimalistic library, something like: cardano-client-serialization.jar
