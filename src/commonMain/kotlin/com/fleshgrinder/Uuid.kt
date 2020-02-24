@file:Suppress("AMBIGUOUS_ACTUALS")

package com.fleshgrinder

/**
 * A Universally Unique Identifier (UUID) as specified in RFC&nbsp;4122.
 *
 * A UUID is a unique 128-bit unsigned integer that guarantees uniqueness across
 * space and time. Its main use case is to assign identifiers to entities
 * without the requirement of a central locking authority. It is thus
 * particularly useful in distributed systems, but can be used in disparate
 * areas, such as databases and network protocols, as well.
 *
 * The uniqueness property is not strictly guaranteed, however, for all
 * practical purposes, it can be assumed that an unintentional collision would
 * be extremely unlikely.
 *
 * ## References
 * - [RFC4122: A Universally Unique IDentifier (UUID) URN Namespace](http://tools.ietf.org/html/rfc4122)
 * - [Wikipedia: Universally Unique Identifier](http://en.wikipedia.org/wiki/Universally_unique_identifier)
 */
public expect class Uuid {
    /**
     * Get the most significant 64 bits of the UUIDs 128 bit unsigned value. The
     * value is always in big endian, regardless of the native byte order.
     */
    public val msb: Long

    /**
     * Get the least significant 64 bits of the UUIDs 128 bit unsigned value.
     * The value is always in big endian, regardless of the native byte order.
     */
    public val lsb: Long

    /**
     * Whether this UUID is equal to the given other object.
     *
     * Two UUIDs are considered to be equal if, and only if, they encapsulate
     * the same 128 bit value.
     */
    public override fun equals(other: Any?): Boolean

    /**
     * The hash code of a UUID is based on the 128 bit value it encapsulates.
     *
     * The hash code is memoized to speed up operations that depend on it.
     */
    public override fun hashCode(): Int

    /**
     * Convert this UUID to an array of bytes.
     *
     * A UUID is a 128 bit unsigned big endian integer that can be represented
     * with 16 bytes.
     */
    public fun toByteArray(): ByteArray

    /**
     * Format this UUID as string.
     *
     * The formal string representation of a UUID is specified in RFC&nbsp;4122
     * on [page 4](https://tools.ietf.org/html/rfc4122#page-4) and defines that
     * the 128 bit value of a UUID is formatted in five hexadecimal groups
     * separated by a dash:
     *
     *     xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
     *
     * The first group consists of eight hex digits, group two, three, and four
     * of four and the fifth group of twelve. The hexadecimal digits `a` to `f`
     * are always lowercase.
     *
     * The string is memoized to speed up operations that depend on it.
     */
    public override fun toString(): String
}

/**
 * Convert this array of bytes to a UUID.
 *
 * A UUID is a 128 bit unsigned big endian integer that can be represented with
 * 16 bytes.
 *
 * @throws IllegalArgumentException if the array is not exactly 16 bytes long.
 */
public fun ByteArray.toUuid(): Uuid =
    requireNotNull(toUuidOrNull()) {
        "Invalid UUID value, expected exactly 16 bytes but got: $size"
    }

/**
 * Try to convert this array of bytes to a UUID and return `null` if it fails.
 *
 * A UUID is a 128 bit unsigned big endian integer that can be represented with
 * 16 bytes.
 */
public expect fun ByteArray.toUuidOrNull(): Uuid?

/**
 * Parse this string as UUID.
 *
 * The formal string representation of a UUID is specified in RFC&nbsp;4122
 * on [page 4](https://tools.ietf.org/html/rfc4122#page-4) and defines that
 * the 128 bit value of a UUID is formatted in five hexadecimal groups
 * separated by a dash:
 *
 *     xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
 *
 * As can be seen in the example the first group consists of eight hex digits,
 * group two, three, and four of four and the last and final group of twelve.
 * The hexadecimal digits `a` to `f` are valid regardless of their casing,
 * meaning `A` to `F` are considered valid.
 *
 * @sample com.fleshgrinder.UuidTest.toUuidSample
 * @throws IllegalArgumentException if parsing fails
 */
public expect fun String.toUuid(): Uuid

/**
 * Try to parse this string as UUID and return `null` if parsing fails.
 *
 * @sample com.fleshgrinder.UuidTest.toUuidOrNullSample
 * @see toUuid
 */
public fun String.toUuidOrNull(): Uuid? =
    try {
        toUuid()
    } catch (_: IllegalArgumentException) {
        null
    }

/**
 * Create new UUID instance with the given 64 bit [most][msb] and [least][lsb]
 * significant big endian bits.
 */
public expect fun uuidOf(msb: Long, lsb: Long): Uuid

/**
 * Create new UUID instance with the given 64 bit [most][msb] and [least][lsb]
 * significant little endian bits.
 */
public expect fun uuidOfLittleEndian(msb: Long, lsb: Long): Uuid

@Suppress("NOTHING_TO_INLINE")
internal inline fun byteArrayOf(msb: Long, lsb: Long): ByteArray =
    byteArrayOf(
        (msb ushr 56).toByte(),
        (msb ushr 48).toByte(),
        (msb ushr 40).toByte(),
        (msb ushr 32).toByte(),
        (msb ushr 24).toByte(),
        (msb ushr 16).toByte(),
        (msb ushr 8).toByte(),
        msb.toByte(),
        (lsb ushr 56).toByte(),
        (lsb ushr 48).toByte(),
        (lsb ushr 40).toByte(),
        (lsb ushr 32).toByte(),
        (lsb ushr 24).toByte(),
        (lsb ushr 16).toByte(),
        (lsb ushr 8).toByte(),
        lsb.toByte()
    )

@Suppress("NOTHING_TO_INLINE")
internal inline fun ByteArray.msb(): Long =
    ((this[0].toLong() and 0xff) shl 56) or
        ((this[1].toLong() and 0xff) shl 48) or
        ((this[2].toLong() and 0xff) shl 40) or
        ((this[3].toLong() and 0xff) shl 32) or
        ((this[4].toLong() and 0xff) shl 24) or
        ((this[5].toLong() and 0xff) shl 16) or
        ((this[6].toLong() and 0xff) shl 8) or
        (this[7].toLong() and 0xff)

@Suppress("NOTHING_TO_INLINE")
internal inline fun ByteArray.lsb(): Long =
    ((this[8].toLong() and 0xff) shl 56) or
        ((this[9].toLong() and 0xff) shl 48) or
        ((this[10].toLong() and 0xff) shl 40) or
        ((this[11].toLong() and 0xff) shl 32) or
        ((this[12].toLong() and 0xff) shl 24) or
        ((this[13].toLong() and 0xff) shl 16) or
        ((this[14].toLong() and 0xff) shl 8) or
        (this[15].toLong() and 0xff)
