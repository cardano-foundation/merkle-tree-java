package org.cardanofoundation.list;

import org.cardanofoundation.util.Hashing;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static com.bloxbean.cardano.client.util.HexUtil.encodeHexString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashedListTest {

    private static Function<String, byte[]> createHasher() {
        return Hashing::sha2_256;
    }

    @Test
    public void testEmpty() {
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", encodeHexString(HashedList.create(List.of(), createHasher()).hash()));
    }

    @Test
    public void testOneItem() {
        assertEquals("c333744e9953ea4990e00a00806a596f6b1a0fc39a8fdf2e9e9bfa622fb58099", encodeHexString(HashedList.create(List.of("dog"), createHasher()).hash()));
    }

    @Test
    public void testTwoItems() {
        assertEquals("9e0fa7d9199bb3f606f53c2ccb548fc1e0181eccb913eeb645eaee7c1b00c0be", encodeHexString(HashedList.create(List.of("dog", "cat"), createHasher()).hash()));
    }

}