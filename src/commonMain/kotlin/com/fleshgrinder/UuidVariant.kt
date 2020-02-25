package com.fleshgrinder

import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * The variant specifies the internal data layout of a [UUID][Uuid].
 *
 * ## References
 * - [RFC&nbsp;4122: Section 4.1.1](https://tools.ietf.org/html/rfc4122#section-4.1.1)
 */
public enum class UuidVariant {
    NCS,
    RFC4122,
    Microsoft,
    Future;

    public companion object {
        @JvmName("of")
        @JvmStatic
        public operator fun invoke(byte: Byte): UuidVariant =
            byte.toInt().let {
                when {
                    it and 0x80 == 0x00 -> NCS
                    it and 0xc0 == 0x80 -> RFC4122
                    it and 0xe0 == 0xc0 -> Microsoft
                    else -> Future
                }
            }
    }
}
