package com.nullgr.core.security.crypto

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * MD5-encode the given [String]  and return result as hex [String]
 */
@Throws(NoSuchAlgorithmException::class)
fun String.toMd5String() = this.toMd5Bytes().toHexDecimalString()

/**
 * MD5-encode the given [String]  and return result as [ByteArray]
 */
@Throws(NoSuchAlgorithmException::class)
fun String.toMd5Bytes(): ByteArray {
    val algorithm = MessageDigest.getInstance("MD5")
    algorithm.reset()
    algorithm.update(this.toByteArray())
    return algorithm.digest()
}

/**
 * SHA512-encode the given [String]  and return result as hex [String]
 */
@Throws(NoSuchAlgorithmException::class)
fun String.toSHA512String() = this.toSHA512Bytes().toHexDecimalString()

/**
 * SHA512-encode the given [String]  and return result as [ByteArray]
 */
@Throws(NoSuchAlgorithmException::class)
fun String.toSHA512Bytes(): ByteArray {
    val algorithm = MessageDigest.getInstance("SHA-512")
    algorithm.reset()
    algorithm.update(this.toByteArray())
    return algorithm.digest()
}

/**
 * Base64-encode the given [String] and return a newly allocated [String] with the result.
 */
@Throws(UnsupportedEncodingException::class)
fun String.toBase64(): String = Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)

/**
 * Decode the Base64-encoded data in input and return the data in
 * a newly allocated [String] with the result.
 */
@Throws(IllegalArgumentException::class)
fun String.fromBase64AsString() = String(Base64.decode(this, Base64.NO_WRAP))

/**
 * Decode the Base64-encoded data in input and return the data in
 * a newly allocated [ByteArray] with the result.
 */
@Throws(IllegalArgumentException::class)
fun String.fromBase64() = Base64.decode(this, Base64.NO_WRAP)

/**
 * Base64-encode the given [ByteArray] and return a newly allocated [String] with the result.
 */
@Throws(UnsupportedEncodingException::class)
fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.NO_WRAP)

/**
 * Decode the Base64-encoded data in input and return the data in
 * a newly allocated [String] with the result.
 */
@Throws(IllegalArgumentException::class)
fun ByteArray.fromBase64AsString() = String(Base64.decode(this, Base64.NO_WRAP))

/**
 * Decode the Base64-encoded data in input and return the data in
 * a newly allocated [ByteArray] with the result.
 */
@Throws(IllegalArgumentException::class)
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
