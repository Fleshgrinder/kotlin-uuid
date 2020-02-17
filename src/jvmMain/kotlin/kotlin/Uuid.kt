@file:JvmName("UuidUtil")
@file:Suppress("NO_ACTUAL_CLASS_MEMBER_FOR_EXPECTED_CLASS", "ACTUAL_WITHOUT_EXPECT")

package kotlin

import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.UUID

@ExperimentalStdlibApi
@SinceKotlin("1.3")
actual typealias Uuid = UUID

@ExperimentalStdlibApi
@SinceKotlin("1.3")
actual inline val Uuid.isNil: Boolean
    @JvmName("isNilUuid")
    get() = mostSignificantBits == 0L && leastSignificantBits == 0L

@ExperimentalStdlibApi
@JvmName("uuidOf")
@SinceKotlin("1.3")
actual fun String.toUuid(): Uuid =
    if (length == 36) Uuid(segmentToLong(0, 19), segmentToLong(19, 36)) else throw IllegalArgumentException(
        "Invalid UUID string, expected exactly 36 characters but got $length: $this"
    )

@ExperimentalStdlibApi
@SinceKotlin("1.3")
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

@ExperimentalStdlibApi
@SinceKotlin("1.3")
@Suppress("NOTHING_TO_INLINE") // @kotlin.internal.InlineOnly
public actual inline fun uuidOf(msb: Long, lsb: Long): Uuid =
    UUID(msb, lsb)

@ExperimentalStdlibApi
@SinceKotlin("1.3")
private fun Uuid.toByteArray(): ByteArray =
    ByteBuffer.allocate(16).putLong(mostSignificantBits).putLong(leastSignificantBits).array()

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public fun uuid3Of(namespace: Uuid, name: String): Uuid =
    UUID.nameUUIDFromBytes(namespace.toByteArray() + name.toByteArray())

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public fun uuid3Of(namespace: Uuid, vararg name: String): Uuid =
    uuid3Of(namespace, name.joinToString(""))

@ExperimentalStdlibApi
@SinceKotlin("1.3")
actual fun uuid4(): Uuid =
    UUID.randomUUID()

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public fun uuid5Of(namespace: Uuid, name: String): Uuid =
    uuid5Of(namespace.toByteArray() + name.toByteArray())

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public fun uuid5Of(namespace: Uuid, vararg names: String): Uuid =
    uuid5Of(namespace, names.joinToString(""))

@ExperimentalStdlibApi
@PublishedApi
@SinceKotlin("1.3")
internal fun uuid5Of(data: ByteArray): Uuid =
    MessageDigest.getInstance("SHA1").digest(data).copyOf(16).let { digest ->
        digest[6] = ((digest[6].toInt() and 0x0f) or 0x50).toByte() // set version to 5
        digest[8] = ((digest[8].toInt() and 0x3f) or 0x80).toByte() // set variant to RFC 4122
        ByteBuffer.wrap(digest).let { UUID(it.long, it.long) }
    }
