package com.fleshgrinder

import kotlin.LazyThreadSafetyMode.NONE

public actual class Uuid internal constructor(private val bytes: ByteArray) {
    private val hash by lazy(NONE) {
        bytes.contentHashCode()
    }

    private val string by lazy(NONE) {
        String(
            charArrayOf(
                bytes.msb(0), bytes.lsb(0),
                bytes.msb(1), bytes.lsb(1),
                bytes.msb(2), bytes.lsb(2),
                bytes.msb(3), bytes.lsb(3),
                '-',
                bytes.msb(4), bytes.lsb(4),
                bytes.msb(5), bytes.lsb(5),
                '-',
                bytes.msb(6), bytes.lsb(6),
                bytes.msb(7), bytes.lsb(7),
                '-',
                bytes.msb(8), bytes.lsb(8),
                bytes.msb(9), bytes.lsb(9),
                '-',
                bytes.msb(10), bytes.lsb(10),
                bytes.msb(11), bytes.lsb(11),
                bytes.msb(12), bytes.lsb(12),
                bytes.msb(13), bytes.lsb(13),
                bytes.msb(14), bytes.lsb(14),
                bytes.msb(15), bytes.lsb(15)
            )
        )
    }

    override fun equals(other: Any?): Boolean =
        other is Uuid && bytes.contentEquals(other.bytes)

    override fun hashCode(): Int =
        hash

    public override fun toString(): String =
        string

    private fun ByteArray.msb(i: Int): Char =
        (this[i].toInt() ushr 4).hex()

    private fun ByteArray.lsb(i: Int): Char =
        this[i].toInt().hex()

    /**
     * We have to operate with [Int]s and cannot directly work with the [Byte]s
     * because Kotlin has no support for directly shifting them like other
     * languages do. This stems from the JVM background, since the JVM always
     * operates on 32 bit words.
     */
    private fun Int.hex(): Char =
        when (val n = this and 0x0f) {
            0 -> '0'
            1 -> '1'
            2 -> '2'
            3 -> '3'
            4 -> '4'
            5 -> '5'
            6 -> '6'
            7 -> '7'
            8 -> '8'
            9 -> '9'
            10 -> 'a'
            11 -> 'b'
            12 -> 'c'
            13 -> 'd'
            14 -> 'e'
            15 -> 'f'
            else -> error("Internal error: encountered non-hexadecimal digit $n")
        }
}

public actual fun String.toUuid(): Uuid {
    require(length == 36) {
        "Invalid UUID string, expected exactly 36 characters gut got $length: $this"
    }

    require(this[8] == '-' && this[13] == '-' && this[18] == '-' && this[23] == '-') {
        "Invalid UUID string, expected dash at index 8, 13, 18, and 23 but got: $this"
    }

    return Uuid(
        byteArrayOf(
            this[0, 1],
            this[2, 3],
            this[4, 5],
            this[6, 7],
            this[9, 10],
            this[11, 12],
            this[14, 15],
            this[16, 17],
            this[19, 20],
            this[21, 22],
            this[24, 25],
            this[26, 27],
            this[28, 29],
            this[30, 31],
            this[32, 33],
            this[34, 35]
        )
    )
}

private operator fun String.get(i: Int, j: Int): Byte =
    ((hex(i) shl 4) or hex(j)).toByte()

private fun String.hex(i: Int): Int =
    when (this[i]) {
        '0' -> 0
        '1' -> 1
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        'a', 'A' -> 10
        'b', 'B' -> 11
        'c', 'C' -> 12
        'd', 'D' -> 13
        'e', 'E' -> 14
        'f', 'F' -> 15
        else -> throw IllegalArgumentException(
            "Invalid UUID string, encountered non-hexadecimal digit `${get(i)}`at index $i in: $this"
        )
    }