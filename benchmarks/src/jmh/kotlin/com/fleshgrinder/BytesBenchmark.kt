package com.fleshgrinder

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode.Throughput
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

/**
 * This benchmark compares a loop based byte array construction vs the manually
 * unrolled one that we use as part of the library. It proofs that our manual
 * approach performs better.
 */
@BenchmarkMode(Throughput)
@State(Scope.Benchmark)
open class BytesBenchmark {
    open var bytes = byteArrayOf(
        0x6b.toByte(),
        0xa7.toByte(),
        0xb8.toByte(),
        0x10.toByte(),
        0x9d.toByte(),
        0xad.toByte(),
        0x11.toByte(),
        0xd1.toByte(),
        0x80.toByte(),
        0xb4.toByte(),
        0x00.toByte(),
        0xc0.toByte(),
        0x4f.toByte(),
        0xd4.toByte(),
        0x30.toByte(),
        0xc8.toByte()
    )

    @Benchmark fun unrolled(): Uuid = bytes.toUuid()
    @Benchmark fun loop(): Uuid = bytes.loopToUuid()
}

// We must define the same indirection as we have with our original toUuid
// function or the comparison is not fair.
private fun ByteArray.loopToUuid(): Uuid =
    requireNotNull(loopToUuidOrNull()) {
        "Invalid UUID value, expected exactly 16 bytes but got $size"
    }

private fun ByteArray.loopToUuidOrNull(): Uuid? =
    if (size != 16) null else run {
        var i = 0

        var msb = 0L
        do {
            msb = (msb shl 8) or (this[i].toLong() and 0xff)
        } while (++i < 8)

        var lsb = 0L
        do {
            lsb = (lsb shl 8) or (this[i].toLong() and 0xff)
        } while (++i < 16)

        @Suppress("INVISIBLE_MEMBER")
        return Uuid(msb, lsb)
    }
