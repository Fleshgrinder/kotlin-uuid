@file:Suppress("AMBIGUOUS_ACTUALS")

package com.fleshgrinder

public expect class Uuid

/**
 * @throws IllegalArgumentException
 */
public expect fun String.toUuid(): Uuid

public fun String.toUuidOrNull(): Uuid? =
    try {
        toUuid()
    } catch (_: IllegalArgumentException) {
        null
    }
