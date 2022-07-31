package com.fleshgrinder.uuid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @see java.util.UUID
 */
public final class Uuid implements Comparable<Uuid>, Serializable {
    private static final long serialVersionUID = 1;
    private final long msb;
    private final long lsb;

    // region ------------------------------------------------------------------ constructors

    public Uuid(final long msb, final long lsb) {
        this.msb = msb;
        this.lsb = lsb;
    }

    /**
     * Constructs a new UUID with all bits set to zero. This special variant is
     * known as the nil UUID, and it can be used instead of {@code null}. This
     * default constructor exists to prepare the implementation for project
     * Valhalla so that we can eventually switch from a normal class to an
     * inline class to minimize the footprint of UUIDs even further.
     */
    public Uuid() {
        this(0, 0);
    }

    /**
     * Constructs a new UUID with the 64 least-significant bits set to the given
     * number. This is useful in testing to have predictable but different UUIDs
     * for different entities while keeping them easily identifiable by humans.
     */
    public Uuid(final long lsb) {
        this(0, lsb);
    }

    /**
     * Constructs a new UUID from the given data. This constructor does not
     * perform any checks on data and simply takes the first 16 bytes from it.
     * See {@link #of(byte[]) Uuid.of} for a public API that verifies that the
     * given data is of the required length.
     */
    private Uuid(final byte[] data) {
        // @formatter:off
        this(
            ((data[0] & 0xffL) << 56) |
                ((data[1] & 0xffL) << 48) |
                ((data[2] & 0xffL) << 40) |
                ((data[3] & 0xffL) << 32) |
                ((data[4] & 0xffL) << 24) |
                ((data[5] & 0xffL) << 16) |
                ((data[6] & 0xffL) << 8) |
                (data[7] & 0xffL),
            ((data[8] & 0xffL) << 56) |
                ((data[9] & 0xffL) << 48) |
                ((data[10] & 0xffL) << 40) |
                ((data[11] & 0xffL) << 32) |
                ((data[12] & 0xffL) << 24) |
                ((data[13] & 0xffL) << 16) |
                ((data[14] & 0xffL) << 8) |
                (data[15] & 0xffL)
        );
        // @formatter:on
    }

    private Uuid(final byte[] data, @Range(from = 0, to = 14) final int v) {
        // @formatter:off
        this(
            ((data[0] & 0xffL) << 56) |
                ((data[1] & 0xffL) << 48) |
                ((data[2] & 0xffL) << 40) |
                ((data[3] & 0xffL) << 32) |
                ((data[4] & 0xffL) << 24) |
                ((data[5] & 0xffL) << 16) |
                (((data[6] & 0x0fL) | (v << 4)) << 8) |
                (data[7] & 0xffL),
            (((data[8] & 0x3fL) | 0x80L) << 56) |
                ((data[9] & 0xffL) << 48) |
                ((data[10] & 0xffL) << 40) |
                ((data[11] & 0xffL) << 32) |
                ((data[12] & 0xffL) << 24) |
                ((data[13] & 0xffL) << 16) |
                ((data[14] & 0xffL) << 8) |
                (data[15] & 0xffL)
        );
        // @formatter:on
    }

    // endregion --------------------------------------------------------------- constructors
    // region ------------------------------------------------------------------ factories

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Uuid of(final byte[] data) {
        final Uuid uuid = ofOrNull(data);
        if (uuid == null) {
            throw new IllegalArgumentException("UUID data must be 16 bytes in length, got: " + data.length);
        }
        return uuid;
    }

    @Contract(pure = true)
    public static @Nullable Uuid ofOrNull(final byte[] data) {
        return data.length == 16 ? new Uuid(data) : null;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Uuid of(final byte[] data, @Range(from = 0, to = 14) final int v) {
        final Uuid uuid = ofOrNull(data, v);
        if (uuid == null) {
            throw new IllegalArgumentException("UUID data must be 16 bytes in length, got: " + data.length);
        }
        return uuid;
    }

    @Contract(pure = true)
    public static @Nullable Uuid ofOrNull(final byte[] data, @Range(from = 0, to = 14) final int v) {
        //noinspection ConstantConditions
        return data.length == 16 && 0 <= v && v <= 14 ? new Uuid(data, v) : null;
    }

    // endregion --------------------------------------------------------------- factories
    // region ------------------------------------------------------------------ parsing

    public static boolean isValid(final @NotNull String data, final boolean strict) {
        if (!strict) {
            // TODO
        }
        return isValid(data);
    }

    public static boolean isValid(final @NotNull String data) {
        // TODO
        return false;
    }

    /**
     * Parses the given data according to different well-known UUID formats and
     * constructs a new {@link Uuid} instance from it.
     *
     * @throws IllegalArgumentException
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Uuid parse(
        @Pattern("(?i)\\A([\\da-f]{32}|[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}|\\{[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}}|(?-i)urn:uuid:(?i)[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12})\\z")
        @Subst("00000000-0000-0000-0000-000000000000") final @NotNull String data
    ) {
        final Uuid uuid = parseOrNull(data);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID string, got: " + data);
        }
        return uuid;
    }

    @Contract(pure = true)
    @SuppressWarnings("PatternValidation")
    public static @Nullable Uuid parseOrNull(
        @Pattern("(?i)\\A([\\da-f]{32}|[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}|\\{[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}}|(?-i)urn:uuid:(?i)[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12})\\z")
        @Subst("00000000-0000-0000-0000-000000000000") final @NotNull String data
    ) {
        final int l = data.length();
        switch (l) {
            // TODO simple form without dashes
            case 32:
                return null;
            case 36:
                return ofOrNull(data);
            case 38:
                return data.charAt(0) == '{' && data.charAt(l - 1) == '}' ? ofOrNull(data.substring(1, l - 1)) : null;
            case 45:
                return data.startsWith("urn:uuid:") ? ofOrNull(data.substring(9)) : null;
        }
        return null;
    }

    public static @NotNull Uuid of(final @NotNull String data, final boolean strict) {
        if (!strict) {
            // TODO using a flag might be better than parse, no?
        }
        return of(data);
    }

    @Contract(pure = true)
    @SuppressWarnings("PatternValidation")
    public static @Nullable Uuid ofOrNull(final @NotNull String data, final boolean strict) {
        if (strict) {
            return ofOrNull(data);
        }

        final int l = data.length();
        switch (l) {
            case 32:
                final StringBuilder sb = new StringBuilder(data);
                sb.insert(8, '-');
                sb.insert(13, '-');
                sb.insert(18, '-');
                sb.insert(23, '-');
                return ofOrNull(sb.toString());

            case 36:
                return ofOrNull(data);

            case 38:
                final int m = l - 1;
                return data.charAt(0) == '{' && data.charAt(m) == '}'
                    ? ofOrNull(data.substring(0, m))
                    : null;

            case 45:
                return data.startsWith("urn:uuid:")
                    ? ofOrNull(data.substring(9))
                    : null;
        }
        return null;
    }

    /**
     * Parses the given data and tries to construct a UUID from it.
     *
     * @throws IllegalArgumentException if the given data is not a valid UUID string.
     */
    @Contract(value = "_ -> new", pure = true)
    @JsonCreator
    public static @NotNull Uuid of(
        @Pattern("(?i)\\A[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}\\z")
        @Subst("00000000-0000-0000-0000-000000000000") final @NotNull String data
    ) {
        final Uuid uuid = ofOrNull(data);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID string, got: " + data);
        }
        return uuid;
    }

    @Contract(pure = true)
    public static @Nullable Uuid ofOrNull(
        @Pattern("(?i)\\A[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}\\z")
        @Subst("00000000-0000-0000-0000-000000000000") final @NotNull String data
    ) {
        if (data.length() == 36 && (data.charAt(8) & data.charAt(13) & data.charAt(18) & data.charAt(23)) == '-') {
            final long a = of(data, 0);
            final long b = of(data, 4);
            final long c = of(data, 9);
            final long d = of(data, 14);
            final long e = of(data, 19);
            final long f = of(data, 24);
            final long g = of(data, 28);
            final long h = of(data, 32);
            if ((a | b | c | d | e | f | g | h) >= 0) {
                return new Uuid(a << 48 | b << 32 | c << 16 | d, e << 48 | f << 32 | g << 16 | h);
            }
        }
        return null;
    }

    public static final byte[] NIBBLES;

    static {
        final byte[] nibbles = new byte[256];
        Arrays.fill(nibbles, (byte) -1);
        nibbles['0'] = 0;
        nibbles['1'] = 1;
        nibbles['2'] = 2;
        nibbles['3'] = 3;
        nibbles['4'] = 4;
        nibbles['5'] = 5;
        nibbles['6'] = 6;
        nibbles['7'] = 7;
        nibbles['8'] = 8;
        nibbles['9'] = 9;
        nibbles['A'] = 10;
        nibbles['a'] = 10;
        nibbles['B'] = 11;
        nibbles['b'] = 11;
        nibbles['C'] = 12;
        nibbles['c'] = 12;
        nibbles['D'] = 13;
        nibbles['d'] = 13;
        nibbles['E'] = 14;
        nibbles['e'] = 14;
        nibbles['F'] = 15;
        nibbles['f'] = 15;
        NIBBLES = nibbles;
    }

    @Contract(pure = true)
    private static long of(final @NotNull String data, final int i) {
        final byte[] n = NIBBLES;
        final char a = data.charAt(i);
        final char b = data.charAt(i + 1);
        final char c = data.charAt(i + 2);
        final char d = data.charAt(i + 3);
        return (a | b | c | d) > 0xff ? -1 : n[a] << 12 | n[b] << 8 | n[c] << 4 | n[d];
    }

    // endregion --------------------------------------------------------------- parsing
    // region ------------------------------------------------------------------ named

    @Contract(value = "_, _, _, _ -> new", pure = true)
    private static @NotNull Uuid named(
        final @NotNull Uuid ns,
        final byte[] n,
        @Range(from = 0, to = 14) final int v,
        final @NotNull String a
    ) {
        // MDs are not thread safe and we cannot pool them.
        // ThreadLocal leaks memory because we cannot close things.
        // TODO can we clone this securely without knowing who we are dealing with?
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(a);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError(a + " not supported.", e);
        }
        md.update(ns.bytes());
        md.update(n);
        return new Uuid(md.digest(), v);
    }

    @Contract(value = "_ -> new", pure = true)
    static byte[] bytes(final int n) {
        // @formatter:off
        return new byte[]{
            (byte) (n >>> 24),
            (byte) (n >>> 16),
            (byte) (n >>> 8),
            (byte) n,
        };
        // @formatter:on
    }

    @Contract(value = "_ -> new", pure = true)
    static byte[] bytes(final long n) {
        // @formatter:off
        return new byte[]{
            (byte) (n >>> 56),
            (byte) (n >>> 48),
            (byte) (n >>> 40),
            (byte) (n >>> 32),
            (byte) (n >>> 24),
            (byte) (n >>> 16),
            (byte) (n >>> 8),
            (byte) n,
        };
        // @formatter:on
    }

    // endregion --------------------------------------------------------------- named
    // region ------------------------------------------------------------------ v1
    // endregion --------------------------------------------------------------- v1
    // region ------------------------------------------------------------------ v2
    // endregion --------------------------------------------------------------- v2
    // region ------------------------------------------------------------------ v3

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Uuid v3(final @NotNull Uuid ns, final int n) {
        return v3(ns, bytes(n));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Uuid v3(final @NotNull Uuid ns, final long n) {
        return v3(ns, bytes(n));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Uuid v3(final @NotNull Uuid ns, final @NotNull String n) {
        return v3(ns, n.getBytes());
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull Uuid v3(final @NotNull Uuid namespace, final @NotNull String name, final @NotNull Charset charset) {
        return v3(namespace, name.getBytes(charset));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Uuid v3(final @NotNull Uuid namespace, final byte @NotNull [] name) {
        return named(namespace, name, 3, "MD5");
    }

    // endregion --------------------------------------------------------------- v3
    // region ------------------------------------------------------------------ v4

    private static class LazySecureRandom {
        private static final SecureRandom[] POOL;

        static {
            final int processors = Runtime.getRuntime().availableProcessors();
            final SecureRandom[] pool = new SecureRandom[processors];
            for (int i = 0; i < processors; ++i) {
                pool[i] = new SecureRandom();
            }
            POOL = pool;
        }
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Uuid v4(final @NotNull SecureRandom secureRandom) {
        final byte[] data = new byte[16];
        secureRandom.nextBytes(data);
        return Uuid.of(data, 4);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Uuid v4(final @NotNull SecureRandom @NotNull [] pool) {
        return v4(pool[(int) (Thread.currentThread().getId() % pool.length)]);
    }

    @Contract(value = "-> new", pure = true)
    public static @NotNull Uuid v4() {
        return v4(LazySecureRandom.POOL);
    }

    // endregion --------------------------------------------------------------- v4
    // region ------------------------------------------------------------------ v5

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Uuid v5(final @NotNull Uuid namespace, final int name) {
        return v5(namespace, bytes(name));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Uuid v5(final @NotNull Uuid namespace, final long name) {
        return v5(namespace, bytes(name));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Uuid v5(final @NotNull Uuid namespace, final @NotNull String name) {
        return v5(namespace, name.getBytes());
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull Uuid v5(final @NotNull Uuid namespace, final @NotNull String name, final @NotNull Charset charset) {
        return v5(namespace, name.getBytes(charset));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Uuid v5(final @NotNull Uuid namespace, final byte @NotNull [] name) {
        return named(namespace, name, 5, "SHA1");
    }

    // endregion --------------------------------------------------------------- v5
    // region ------------------------------------------------------------------ v6
    // endregion --------------------------------------------------------------- v6
    // region ------------------------------------------------------------------ v7
    // endregion --------------------------------------------------------------- v7
    // region ------------------------------------------------------------------ v8
    // endregion --------------------------------------------------------------- v8
    // region ------------------------------------------------------------------ accessors

    @Contract(pure = true)
    public long getMostSignificantBits() {
        return msb;
    }

    @Contract(pure = true)
    public long getLeastSignificantBits() {
        return lsb;
    }

    @Contract(pure = true)
    @Range(from = 0, to = 7)
    public int getVariant() {
        if (getVersion() != 1) {
            // TODO proper message
            throw new IllegalStateException("");
        }
        final long lsb = this.lsb;
        return (int) ((lsb >>> (64 - (lsb >>> 62))) & (lsb >> 63));
    }

    @Contract(pure = true)
    @Range(from = 0, to = 14)
    public int getVersion() {
        return (int) ((msb >> 12) & 0x0f);
    }

    // endregion --------------------------------------------------------------- accessors
    // region ------------------------------------------------------------------ converters

    @Contract(pure = true)
    public byte @NotNull [] bytes() {
        final long msb = this.msb;
        final long lsb = this.lsb;
        // @formatter:off
        return new byte[]{
            (byte) (msb >>> 56),
            (byte) (msb >>> 48),
            (byte) (msb >>> 40),
            (byte) (msb >>> 32),
            (byte) (msb >>> 24),
            (byte) (msb >>> 16),
            (byte) (msb >>> 8),
            (byte) msb,
            (byte) (lsb >>> 56),
            (byte) (lsb >>> 48),
            (byte) (lsb >>> 40),
            (byte) (lsb >>> 32),
            (byte) (lsb >>> 24),
            (byte) (lsb >>> 16),
            (byte) (lsb >>> 8),
            (byte) lsb,
        };
        // @formatter:on
    }

    @Contract(pure = true)
    @Override
    @JsonValue
    @Pattern("\\A[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}\\z")
    @Subst("00000000-0000-0000-0000-000000000000")
    public @NotNull String toString() {
        // We cannot reimplement nor beat the UUID.toString implementation in
        // userland because it uses special internal APIs to construct the
        // String without going through validations that userland code always
        // has to go through. We thus use it here and leave it to the JVM to
        // optimize the object construction away. Which it does, since it
        // understands that the object is never used for nothing here.
        //
        //noinspection PatternValidation
        return new java.util.UUID(msb, lsb).toString();
    }

    // endregion --------------------------------------------------------------- converters
    // region ------------------------------------------------------------------ object

    @Contract(pure = true)
    @Override
    public int compareTo(final @NotNull Uuid other) {
        final int r = Long.compareUnsigned(msb, other.msb);
        return r == 0 ? Long.compareUnsigned(lsb, other.lsb) : r;
    }

    @Contract(pure = true)
    @Override
    public int hashCode() {
        return Long.hashCode(msb ^ lsb);
    }

    @Contract(pure = true)
    @Override
    public boolean equals(final @Nullable Object other) {
        return other instanceof Uuid && msb == ((Uuid) other).msb && lsb == ((Uuid) other).lsb;
    }

    // endregion --------------------------------------------------------------- object
}
