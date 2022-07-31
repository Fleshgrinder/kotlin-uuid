package com.fleshgrinder.uuid.internal

import com.fleshgrinder.uuid.Uuid
import com.fleshgrinder.uuid.toByteArray
import java.security.MessageDigest
import java.security.SecureRandom

private sealed class DigestHolder(algo: String) {
    val md: MessageDigest = MessageDigest.getInstance(algo)

    fun hash(ns: Uuid, n: ByteArray): ByteArray {
        val buf = ByteArray(16)
        md.update(ns.toByteArray())
        md.update(n)
        md.digest(buf, 0, 16)
        return buf
    }

    object MD5 : DigestHolder("MD5")
    object SHA1 : DigestHolder("SHA1")
}

@PublishedApi
internal actual fun md5(ns: Uuid, n: ByteArray): ByteArray =
    DigestHolder.MD5.hash(ns, n)

@PublishedApi
internal actual fun sha1(ns: Uuid, n: ByteArray): ByteArray =
    DigestHolder.SHA1.hash(ns, n)

private object SecureRandomHolder {
    val value: SecureRandom = SecureRandom.getInstanceStrong()
}

@PublishedApi
internal actual fun rand16(): ByteArray {
    val buf = ByteArray(16)
    SecureRandomHolder.value.nextBytes(buf)
    return buf
}
