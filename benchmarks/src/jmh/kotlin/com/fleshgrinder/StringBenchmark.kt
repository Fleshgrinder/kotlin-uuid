package com.fleshgrinder

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode.Throughput
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import java.util.UUID

/**
 * This benchmark compares the Java UUID parser with our implementation and
 * proofs that our approach is faster.
 */
@BenchmarkMode(Throughput)
@State(Scope.Benchmark)
open class StringBenchmark {
    open var uuid = "6ba7b811-9dad-11d1-80b4-00c04fd430c8"
    @Benchmark fun java(): UUID = UUID.fromString(uuid)
    @Benchmark fun kotlin(): Uuid = uuid.toUuid()
}
