@file:JvmName("UuidFactory")

package com.fleshgrinder

import java.util.UUID
import kotlin.LazyThreadSafetyMode.PUBLICATION

public actual class Uuid internal constructor(private val msb: Long, private val lsb: Long) {
    /**
     * We cannot beat Java‘s UUID string formatter on newer versions because it
     * uses a lot of internal machinery, that is not available to us, to build
     * the string with as little overhead as possible. Note that the extra
     * object creation is something the JIT will optimize away if invoked often
     * enough. We also memoize the result to speed things up even further.
     */
    private val string by lazy(PUBLICATION) { UUID(msb, lsb).toString() }

    public actual override fun equals(other: Any?): Boolean =
        other is Uuid && msb == other.msb && lsb == other.lsb

    public actual override fun hashCode(): Int =
        (msb or lsb).let { (it shr 32).toInt() or it.toInt() }

    public actual override fun toString(): String =
        string

    public companion object {
        /**
         * Parse the given string as UUID.
         *
         * The formal string representation of a UUID is specified in RFC&nbsp;4122 on
         * [page 4](https://tools.ietf.org/html/rfc4122#page-4) and defines that a UUID
         * is made up of five hexadecimal groups separated by a dash:
         *
         *     xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
         *
         * As can be seen in the example the first group consists of eight hex digits,
         * group two, three, and four of four and the last and final group of twelve.
         * The hexadecimal digits `a` to `f` are valid regardless of their casing,
         * meaning `A` to `F` are considered valid.
         *
         * ```java
         * final Uuid uuid = Uuid.parse("F81D4FAE-7DEC-11D0-A765-00A0C91E6BF6");
         * System.out.println(uuid);
         * // f81d4fae-7dec-11d0-a765-00a0c91e6bf6
         * ```
         *
         * @throws IllegalArgumentException if parsing fails
         */
        @JvmStatic
        // https://youtrack.jetbrains.com/issue/KT-36439
        @SinceKotlin("999999.999999")
        @Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
        public fun parse(s: String): Uuid =
            s.toUuid()
    }
}

@JvmSynthetic
public actual fun String.toUuid(): Uuid =
    if (length == 36) Uuid(segmentToLong(0, 19), segmentToLong(19, 36))
    else throw IllegalArgumentException("Invalid UUID string, expected exactly 36 characters but got $length: $this")

private fun String.segmentToLong(start: Int, end: Int): Long {
    var result = 0L

    var i = start
    do {
        if (this[i] == '-') {
            require(i == 8 || i == 13 || i == 18 || i == 23) {
                "Invalid UUID string, encountered dash at index $i but it can occur only at 8, 13, 18, or 23: $this"
            }
        } else {
            result *= 16
            when (this[i]) {
                '0' -> Unit
                '1' -> result += 1L
                '2' -> result += 2L
                '3' -> result += 3L
                '4' -> result += 4L
                '5' -> result += 5L
                '6' -> result += 6L
                '7' -> result += 7L
                '8' -> result += 8L
                '9' -> result += 9L
                'a', 'A' -> result += 10L
                'b', 'B' -> result += 11L
                'c', 'C' -> result += 12L
                'd', 'D' -> result += 13L
                'e', 'E' -> result += 14L
                'f', 'F' -> result += 15L
                else -> throw IllegalArgumentException(
                    "Invalid UUID string, encountered non-hexadecimal digit `${this[i]}` at index $i in: $this"
                )
            }
        }
    } while (++i < end)

    return result
}
