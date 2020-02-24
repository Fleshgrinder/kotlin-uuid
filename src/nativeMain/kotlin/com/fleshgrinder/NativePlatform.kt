package com.fleshgrinder

import kotlin.native.concurrent.freeze as nativeFreeze

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun Any.freeze() {
    nativeFreeze()
}
