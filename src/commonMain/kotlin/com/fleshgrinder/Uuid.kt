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
