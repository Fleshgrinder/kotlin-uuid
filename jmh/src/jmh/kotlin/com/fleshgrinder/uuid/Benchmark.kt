package com.fleshgrinder.uuid

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole
import java.util.*

@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 2)
open class Benchmark {
    @Param("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF")
    open lateinit var a: String

    @Param("ffffffff-ffff-ffff-ffff-ffffffffffff")
    open lateinit var b: String

    @Benchmark
    @Suppress("DuplicatedCode")
    fun UUID(void: Blackhole) {
        val a = UUID.fromString(a)
        val b = UUID.fromString(b)

        void.consume(a < b)
        void.consume(a <= b)
        void.consume(a == b)
        void.consume(a >= b)
        void.consume(a > b)

        void.consume(a.hashCode())
        void.consume(b.hashCode())

        void.consume(a.toString())
        void.consume(b.toString())
    }

    @Benchmark
    @Suppress("DuplicatedCode")
    fun Uuid(void: Blackhole) {
        val a = Uuid.of(a)
        val b = Uuid.of(b)

        void.consume(a < b)
        void.consume(a <= b)
        void.consume(a == b)
        void.consume(a >= b)
        void.consume(a > b)

        void.consume(a.hashCode())
        void.consume(b.hashCode())

        void.consume(a.toString())
        void.consume(b.toString())
    }
}
