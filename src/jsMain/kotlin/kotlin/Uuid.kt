package kotlin

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public actual class Uuid : Comparable<Uuid> {
    public actual val mostSignificantBits: Long
        get() = TODO("Not yet implemented")

    public actual val leastSignificantBits: Long
        get() = TODO("Not yet implemented")

    public actual val isNil: Boolean
        get() = TODO("Not yet implemented")

    public actual fun variant(): Int =
        TODO("Not yet implemented")

    public actual fun version(): Int =
        TODO("Not yet implemented")

    // We have to compare with signed values for JVM compatibility.
    public override fun compareTo(other: Uuid): Int =
        TODO("Not yet implemented")

    public override fun equals(other: Any?): Boolean =
        TODO("Not yet implemented")

    public override fun hashCode(): Int =
        TODO("Not yet implemented")

    public override fun toString(): String =
        TODO("Not yet implemented")
}

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public actual fun String.toUuid(): Uuid =
    TODO("Not yet implemented")

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public actual fun uuidOf(msb: Long, lsb: Long): Uuid =
    TODO("Not yet implemented")
