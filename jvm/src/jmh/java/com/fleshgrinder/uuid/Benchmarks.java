package com.fleshgrinder.uuid;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.UUID;

@State(Scope.Benchmark)
public class Benchmarks {
    @Param("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF")
    public String a;

    @Param("ffffffff-ffff-ffff-ffff-ffffffffffff")
    public String b;

    @Benchmark
    public void java(final Blackhole bh) {
        var a = UUID.fromString(this.a);
        var b = UUID.fromString(this.b);

        bh.consume(a.compareTo(b));
        bh.consume(a.equals(b));

        bh.consume(a.hashCode());
        bh.consume(b.hashCode());

        bh.consume(a.toString());
        bh.consume(b.toString());
    }

    @Benchmark
    public void custom(final Blackhole bh) {
        var a = Uuid.of(this.a);
        var b = Uuid.of(this.b);

        bh.consume(a.compareTo(b));
        bh.consume(a.equals(b));

        bh.consume(a.hashCode());
        bh.consume(b.hashCode());

        bh.consume(a.toString());
        bh.consume(b.toString());
    }
}
