@file:Suppress("NOTHING_TO_INLINE")

package com.fleshgrinder.uuid

import com.fleshgrinder.uuid.internal.md5
import com.fleshgrinder.uuid.internal.rand16
import com.fleshgrinder.uuid.internal.sha1
import com.fleshgrinder.uuid.internal.toByteArray

@JvmInline
public expect value class Uuid @PublishedApi internal constructor(
    @PublishedApi internal val value: String,
) : Comparable<Uuid> {
    public companion object
}

public val Uuid.msb: Long get() = TODO()

public val Uuid.lsb: Long get() = TODO()

public val Uuid.variant: Int get() = TODO()

public val Uuid.version: Int get() = TODO()

public fun Uuid.toByteArray(): ByteArray = TODO()

public inline fun Uuid.Companion.min(): Uuid =
    Uuid("00000000-0000-0000-0000-000000000000")

public inline fun Uuid.Companion.max(): Uuid =
    Uuid("ffffffff-ffff-ffff-ffff-ffffffffffff")

@ExperimentalUnsignedTypes
public inline operator fun Uuid.Companion.invoke(data: UByteArray): Uuid =
    Uuid(data.toByteArray())

public operator fun Uuid.Companion.invoke(data: ByteArray): Uuid =
    TODO()

public operator fun Uuid.Companion.invoke(msb: Long, lsb: Long): Uuid =
    TODO()

public inline operator fun Uuid.Companion.invoke(lsb: Long): Uuid =
    Uuid(0, lsb)

public operator fun Uuid.Companion.invoke(a: Int, b: Int, c: Int, d: Int): Uuid =
    TODO()

public inline operator fun Uuid.Companion.invoke(d: Int): Uuid =
    Uuid(0, 0, 0, d)

public fun Uuid.Companion.parse(data: String): Uuid {
    TODO()
}

public fun Uuid.Companion.parseOrNull(data: String): Uuid? {
    TODO()
}

@PublishedApi
internal fun Uuid.Companion.v(v: Int, data: ByteArray): Uuid {
    data[6] = ((data[6].toInt() and 0x0f) or (v shl 4)).toByte()
    data[8] = ((data[8].toInt() and 0x3f) or 0x80).toByte()
    return Uuid(data)
}

public fun Uuid.Companion.v1(): Uuid {
    TODO()
}

public fun Uuid.Companion.v2(): Uuid {
    TODO()
}

public inline fun Uuid.Companion.v3(ns: Uuid, n: UInt): Uuid = v3(ns, n.toInt())
public inline fun Uuid.Companion.v3(ns: Uuid, n: Int): Uuid = v3(ns, n.toByteArray())
public inline fun Uuid.Companion.v3(ns: Uuid, n: ULong): Uuid = v3(ns, n.toLong())
public inline fun Uuid.Companion.v3(ns: Uuid, n: Long): Uuid = v3(ns, n.toByteArray())
public inline fun Uuid.Companion.v3(ns: Uuid, n: String): Uuid = v3(ns, n.encodeToByteArray())
public inline fun Uuid.Companion.v3(ns: Uuid, n: ByteArray): Uuid = v(3, md5(ns, n))

public inline fun Uuid.Companion.v4(): Uuid = v(4, rand16())

public inline fun Uuid.Companion.v5(ns: Uuid, n: UInt): Uuid = v5(ns, n.toInt())
public inline fun Uuid.Companion.v5(ns: Uuid, n: Int): Uuid = v5(ns, n.toByteArray())
public inline fun Uuid.Companion.v5(ns: Uuid, n: ULong): Uuid = v5(ns, n.toLong())
public inline fun Uuid.Companion.v5(ns: Uuid, n: Long): Uuid = v5(ns, n.toByteArray())
public inline fun Uuid.Companion.v5(ns: Uuid, n: String): Uuid = v5(ns, n.encodeToByteArray())
public inline fun Uuid.Companion.v5(ns: Uuid, n: ByteArray): Uuid = v(5, sha1(ns, n))

public fun Uuid.Companion.v6(): Uuid = TODO()

public fun Uuid.Companion.v7(): Uuid = TODO()

public inline fun Uuid.Companion.v8(data: ByteArray): Uuid = v(8, data)
