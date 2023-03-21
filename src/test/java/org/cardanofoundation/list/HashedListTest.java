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
        assertEquals("778a58564343a3eb1b4f22abde3ee16a1846ed730365d32334c5b64338c35a2c", encodeHexString(HashedList.create(List.of("dog"), createHasher()).hash()));
    }

    @Test
    public void testTwoItems() {
        assertEquals("abc48c555c2e7fb968c02bf0a6e6854b7239b3ede1d5745152cc656e430ae845", encodeHexString(HashedList.create(List.of("dog", "cat"), createHasher()).hash()));
    }

    @Test
    public void testFourItems() {
        assertEquals("f3b80d721103a0a321bc3b88a946c9c9fc86f5721b61cf012d731b6a1d9efe3b", encodeHexString(HashedList.create(List.of("dog", "cat", "horse", "mouse"), createHasher()).hash()));
    }

}
