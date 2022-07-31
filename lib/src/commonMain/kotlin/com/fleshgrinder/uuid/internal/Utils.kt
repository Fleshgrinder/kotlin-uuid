@file:Suppress("NOTHING_TO_INLINE")

package com.fleshgrinder.uuid.internal

@PublishedApi
internal fun Int.toByteArray(): ByteArray =
    byteArrayOf(
        (this ushr 24).toByte(),
        (this ushr 16).toByte(),
        (this ushr 8).toByte(),
        toByte(),
    )

@PublishedApi
internal fun Long.toByteArray(): ByteArray =
    byteArrayOf(
        (this ushr 56).toByte(),
        (this ushr 48).toByte(),
        (this ushr 40).toByte(),
        (this ushr 32).toByte(),
        (this ushr 24).toByte(),
        (this ushr 16).toByte(),
        (this ushr 8).toByte(),
        toByte(),
    )
