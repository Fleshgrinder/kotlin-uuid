package kotlin

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public expect class Uuid : Comparable<Uuid> {
    /**
     * Get the most-significant bits of this UUID.
     */
    public val mostSignificantBits: Long

    /**
     * Get the least-significant bits of this UUID.
     */
    public val leastSignificantBits: Long

    /**
     * Whether this is the special nil UUID, or not.
     */
    public val isNil: Boolean

    /**
     * Returns the variant of the UUID structure.
     *
     * This determines the interpretation of the structure of the UUID. This
     * implementation only supports RFC 4122.
     *
     * - [Variant Reference](http://tools.ietf.org/html/rfc4122#section-4.1.1)
     */
    public fun variant(): Int

    /**
     * Returns the version of the UUID.
     *
     * This represents the algorithm used to generate the contents.
     *
     * - [Version Reference](http://tools.ietf.org/html/rfc4122#section-4.1.3)
     */
    public fun version(): Int
}

/**
 * [UUID][Uuid] namespace for the Domain Name System (DNS) as specified in
 * [RFC 4122 Appendix C](https://tools.ietf.org/html/rfc4122#appendix-C):
 *
 *     6ba7b810-9dad-11d1-80b4-00c04fd430c8
 */
@ExperimentalStdlibApi
@SinceKotlin("1.3")
public val UUID_NAMESPACE_DNS: Uuid = uuidOf(7757371264673321425, -9172705715073830712)

/**
 * [UUID][Uuid] namespace for ISO Object Identifiers (OIDs) as specified in
 * [RFC 4122 Appendix C](https://tools.ietf.org/html/rfc4122#appendix-C):
 *
 *     6ba7b812-9dad-11d1-80b4-00c04fd430c8
 */
@ExperimentalStdlibApi
@SinceKotlin("1.3")
public val UUID_NAMESPACE_OID: Uuid = uuidOf(7757371273263256017, -9172705715073830712)

/**
 * [UUID][Uuid] namespace for Uniform Resource Locators (URLs) as specified in
 * [RFC 4122 Appendix C](https://tools.ietf.org/html/rfc4122#appendix-C):
 *
 *     6ba7b811-9dad-11d1-80b4-00c04fd430c8
 */
@ExperimentalStdlibApi
@SinceKotlin("1.3")
public val UUID_NAMESPACE_URL: Uuid = uuidOf(7757371268968288721, -9172705715073830712)

/**
 * [UUID][Uuid] namespace for X.500 Distinguished Names (DNs) as specified in
 * [RFC 4122 Appendix C](https://tools.ietf.org/html/rfc4122#appendix-C):
 *
 *     6ba7b814-9dad-11d1-80b4-00c04fd430c8
 */
@ExperimentalStdlibApi
@SinceKotlin("1.3")
public val UUID_NAMESPACE_X500: Uuid = uuidOf(7757371281853190609, -9172705715073830712)

/**
 * Special nil [UUID][Uuid] that has all 128 bits set to zero as specified in
 * [RFC 4122 Section 4.1.7](https://tools.ietf.org/html/rfc4122#section-4.1.7):
 *
 *     00000000-0000-0000-0000-000000000000
 */
@ExperimentalStdlibApi
@SinceKotlin("1.3")
public val UUID_NIL: Uuid = uuidOf(0, 0)

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public expect fun String.toUuid(): Uuid

@ExperimentalStdlibApi
@SinceKotlin("1.3")
public expect fun uuidOf(msb: Long, lsb: Long): Uuid
