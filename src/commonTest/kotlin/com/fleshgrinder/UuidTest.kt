package com.fleshgrinder

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UuidTest {
    @Test fun toUuidSample() {
        assertEquals(
            "f81d4fae-7dec-11d0-a765-00a0c91e6bf6",
            "F81D4FAE-7DEC-11D0-A765-00A0C91E6BF6".toUuid().toString()
        )

        assertFailsWith<IllegalArgumentException> {
            "f81d4fae7dec11d0a76500a0c91e6bf6".toUuid()
        }
    }

    @Test fun toUuidOrNullSample() {
        assertNull("f81d4fae7dec11d0a76500a0c91e6bf6".toUuidOrNull())
    }

    @Test fun parsing() {
        "00000000-0000-0000-0000-000000000000".let { assertEquals(it, it.toUuid().toString(), it) }
        "11111111-1111-1111-1111-111111111111".let { assertEquals(it, it.toUuid().toString(), it) }
        "22222222-2222-2222-2222-222222222222".let { assertEquals(it, it.toUuid().toString(), it) }
        "33333333-3333-3333-3333-333333333333".let { assertEquals(it, it.toUuid().toString(), it) }
        "44444444-4444-4444-4444-444444444444".let { assertEquals(it, it.toUuid().toString(), it) }
        "55555555-5555-5555-5555-555555555555".let { assertEquals(it, it.toUuid().toString(), it) }
        "66666666-6666-6666-6666-666666666666".let { assertEquals(it, it.toUuid().toString(), it) }
        "77777777-7777-7777-7777-777777777777".let { assertEquals(it, it.toUuid().toString(), it) }
        "88888888-8888-8888-8888-888888888888".let { assertEquals(it, it.toUuid().toString(), it) }
        "99999999-9999-9999-9999-999999999999".let { assertEquals(it, it.toUuid().toString(), it) }
        "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa".let { assertEquals(it, it.toUuid().toString(), it) }
        "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb".let { assertEquals(it, it.toUuid().toString(), it) }
        "cccccccc-cccc-cccc-cccc-cccccccccccc".let { assertEquals(it, it.toUuid().toString(), it) }
        "dddddddd-dddd-dddd-dddd-dddddddddddd".let { assertEquals(it, it.toUuid().toString(), it) }
        "eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee".let { assertEquals(it, it.toUuid().toString(), it) }
        "ffffffff-ffff-ffff-ffff-ffffffffffff".let { assertEquals(it, it.toUuid().toString(), it) }
        "AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA".let { assertEquals(it.toLowerCase(), it.toUuid().toString(), it) }
        "BBBBBBBB-BBBB-BBBB-BBBB-BBBBBBBBBBBB".let { assertEquals(it.toLowerCase(), it.toUuid().toString(), it) }
        "CCCCCCCC-CCCC-CCCC-CCCC-CCCCCCCCCCCC".let { assertEquals(it.toLowerCase(), it.toUuid().toString(), it) }
        "DDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD".let { assertEquals(it.toLowerCase(), it.toUuid().toString(), it) }
        "EEEEEEEE-EEEE-EEEE-EEEE-EEEEEEEEEEEE".let { assertEquals(it.toLowerCase(), it.toUuid().toString(), it) }
        "FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF".let { assertEquals(it.toLowerCase(), it.toUuid().toString(), it) }
    }

    @Test fun equality() {
        val a = "00000000-0000-0000-0000-000000000000".toUuid()
        val b = "00000000-0000-0000-0000-000000000000".toUuid()
        val c = "00000000-0000-0000-0000-000000000000".toUuid()

        assertEquals(a, a, "reflexive")
        assertEquals(a == b, b == a, "symmetric")
        assertTrue(a == b && b == c && c == a, "transitive")

        assertEquals(a.hashCode(), a.hashCode(), "reflexive hash")
        assertEquals(a.hashCode(), b.hashCode(), "symmetric hash")

        @Suppress("ReplaceCallWithBinaryOperator")
        assertFalse(a.equals(null), "null")

        val x = "00000000-0000-0000-0000-000000000001".toUuid()
        assertNotEquals(a, x, "a == x")
        assertNotEquals(x, a, "x == a")
        assertNotEquals(b, x, "b == x")
        assertNotEquals(x, b, "x == b")

        assertNotEquals(a.hashCode(), x.hashCode(), "a/x hash")
    }

    @Test fun bytes() {
        val bytes = byteArrayOf(
            0x6b.toByte(),
            0xa7.toByte(),
            0xb8.toByte(),
            0x10.toByte(),
            0x9d.toByte(),
            0xad.toByte(),
            0x11.toByte(),
            0xd1.toByte(),
            0x80.toByte(),
            0xb4.toByte(),
            0x00.toByte(),
            0xc0.toByte(),
            0x4f.toByte(),
            0xd4.toByte(),
            0x30.toByte(),
            0xc8.toByte()
        )
        val uuid = bytes.toUuid()

        assertEquals("6ba7b810-9dad-11d1-80b4-00c04fd430c8", uuid.toString())
        assertTrue(bytes.contentEquals(uuid.toByteArray()))
    }

    @Test fun bits() {
        val be = uuidOf(-1475028236756628487, -8924160304517910252)
        assertEquals("eb87a5a2-3a73-4bf9-8427-037c3a917114", be.toString())
        assertEquals(-1475028236756628487, be.msb)
        assertEquals(-8924160304517910252, be.lsb)

        val le = uuidOfLittleEndian(-483165839338141717, 1473118233501575044)
        assertEquals("eb87a5a2-3a73-4bf9-8427-037c3a917114", le.toString())
        assertEquals(-1475028236756628487, le.msb)
        assertEquals(-8924160304517910252, le.lsb)
    }
}
