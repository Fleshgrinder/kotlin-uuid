package com.fleshgrinder.uuid;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class UuidTest {
    private static final String DNS_VALUE = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";
    private static final Uuid DNS = Uuid.of(DNS_VALUE);

    @Test
    void test_uuid_parsing() {
        assertEquals(DNS_VALUE, DNS.toString());
    }

    @Test
    void test_int_to_bytes() {
        var expected = ByteBuffer.allocate(Integer.BYTES).putInt(Integer.MAX_VALUE).array();
        var actual = Uuid.bytes(Integer.MAX_VALUE);
        assertArrayEquals(expected, actual);
    }

    @Test
    void test_long_to_bytes() {
        var expected = ByteBuffer.allocate(Long.BYTES).putLong(Long.MAX_VALUE).array();
        var actual = Uuid.bytes(Long.MAX_VALUE);
        assertArrayEquals(expected, actual);
    }

    @Test
    void test_uuid_v3_from_string() {
        var expected = "04738bdf-b25a-3829-a801-b21a1d25095b";
        var actual = Uuid.v3(DNS, "example.org").toString();
        assertEquals(expected, actual);
    }
}
