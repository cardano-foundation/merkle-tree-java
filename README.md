# Merkle Tree implementation in Java

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
    val tree = new MerkleTree();

    tree.appendLeaf(Hashing.sha2_256("dog"));
    tree.appendLeaf(Hashing.sha2_256("cat"));
    tree.appendLeaf(Hashing.sha2_256("mouse"));
    tree.appendLeaf(Hashing.sha2_256("horse"));

    val root = tree.buildTree();

    val rootHash = HexFormat.of().formatHex(root);

    assertEquals("bd80e6bec9c2ef6158cf6a74f7f87531e94e0a824b9ba6db28c9a00ba418d452", rootHash);
```

## TODO
- more unit tests
- java docs
- publish to maven central 
- another maven module doing serialisation?
- merkle proof handling
- bloxbean library should have minimalistic interface for PlutusDataSerializer (change in bloxbean lib)