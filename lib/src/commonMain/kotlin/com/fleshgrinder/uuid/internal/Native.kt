package com.fleshgrinder.uuid.internal

import com.fleshgrinder.uuid.Uuid

/**
 * Hashes the given namespace and name with MD5.
 */
@PublishedApi
internal expect fun md5(ns: Uuid, n: ByteArray): ByteArray

/**
 * Hashes the given namespace and name with SHA1.
 */
@PublishedApi
internal expect fun sha1(ns: Uuid, n: ByteArray): ByteArray

/**
 * Gets 16 random Bytes.
 */
@PublishedApi
internal expect fun rand16(): ByteArray
