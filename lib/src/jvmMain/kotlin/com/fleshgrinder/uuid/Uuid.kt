package com.fleshgrinder.uuid

import java.io.Serializable

@JvmInline
public actual value class Uuid @PublishedApi internal actual constructor(
    @PublishedApi internal actual val value: String
) : Comparable<Uuid>, Serializable {
    override fun compareTo(other: Uuid): Int =
        value.compareTo(other.value)

    override fun toString(): String =
        value

    public actual companion object
}
