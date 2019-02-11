package com.nullgr.core.security.crypto

import com.nullgr.core.security.crypto.internal.AesCbcCryptonImpl
import com.nullgr.core.security.crypto.internal.PbeCryptonImpl
import com.nullgr.core.security.crypto.internal.RsaCryptonImpl


/**
 * Simple class to encrypt/decrypt user data with few common algorithms.
 *
 * Available encryption:
 * - Password based AES/CBC/PKCS7Padding encryption/decryption
 * - SecretKey based AES/CBC/PKCS7Padding encryption/decryption (For generation of AES key, [CryptoKeysFactory] can be used)
 * - RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING based encryption/decryption
 *
 * Note. This is an open source project, and anyone can see this code,
 * so if you really need bullet-proof encryption,
 * It is better to use your own encryption-decryption mechanisms or
 * libraries provided by companies specializing in security.
 *
 * @author Grishko Nikita
 */
object Crypton {

    fun passwordBasedEncryption() = PbeCryptonImpl

    fun aesCbcEncryption() = AesCbcCryptonImpl

    fun rsaEncryption() = RsaCryptonImpl
}