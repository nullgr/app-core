package com.nullgr.core.security.crypto

import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * MD5-encode the given [String]  and return result as hex [String]
 */
fun String.toMd5String() = this.toMd5Bytes().toHexDecimalString()

/**
 * MD5-encode the given [String]  and return result as [ByteArray]
 */
fun String.toMd5Bytes(): ByteArray {
    return try {
        val algorithm = MessageDigest.getInstance("MD5")
        algorithm.reset()
        algorithm.update(this.toByteArray())
        algorithm.digest()
    } catch (ex: Throwable) {
        ex.printStackTrace()
        throw RuntimeException(ex)
    }
}

/**
 * SHA512-encode the given [String]  and return result as hex [String]
 */
fun String.toSHA512String() = this.toSHA512Bytes().toHexDecimalString()

/**
 * SHA512-encode the given [String]  and return result as [ByteArray]
 */
fun String.toSHA512Bytes(): ByteArray {
    return try {
        val algorithm = MessageDigest.getInstance("SHA-512")
        algorithm.reset()
        algorithm.update(this.toByteArray())
        algorithm.digest()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        throw RuntimeException(e)
    }
}

/**
 * Base64-encode the given [String] and return a newly allocated [String] with the result.
 */
fun String.toBase64(): String = Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)

/**
 * Decode the Base64-encoded data in input and return the data in
 * a newly allocated [String] with the result.
 */
fun String.fromBase64() = String(Base64.decode(this, Base64.NO_WRAP))

/**
 * Base64-encode the given [ByteArray] and return a newly allocated [String] with the result.
 */
fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.NO_WRAP)

/**
 * Decode the Base64-encoded data in input and return the data in
 * a newly allocated [String] with the result.
 */
fun ByteArray.fromBase64() = String(Base64.decode(this, Base64.NO_WRAP))

/**
 * Convert [ByteArray] to hex [String]
 */
fun ByteArray.toHexDecimalString() = StringBuffer().apply {
    this@toHexDecimalString.forEach {
        append(String.format("%02X", it))
    }
}.toString().toLowerCase()

/**
 * Decode hex [String] to original [String]
 */
fun String.fromHexDecimalString(): String {
    var str = ""
    var i = 0
    while (i < this.length) {
        val s = this.substring(i, i + 2)
        val decimal = Integer.parseInt(s, 16)
        str += decimal.toChar()
        i += 2
    }
    return str
}
