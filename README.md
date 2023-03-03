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
var tree = new MerkleTree();

tree.appendLeaf(MerkleHash.create("dog"));
tree.appendLeaf(MerkleHash.create("cat"));
tree.appendLeaf(MerkleHash.create("mouse"));
tree.appendLeaf(MerkleHash.create("horse"));

var root = tree.buildTree();
var rootHash = HexFormat.of().formatHex(root.getValue());

System.out.println(rootHash);
// bd80e6bec9c2ef6158cf6a74f7f87531e94e0a824b9ba6db28c9a00ba418d452 
```
