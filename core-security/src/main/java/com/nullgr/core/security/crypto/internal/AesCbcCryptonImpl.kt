package com.nullgr.core.security.crypto.internal

import com.nullgr.core.security.crypto.CryptoKeysFactory
import com.nullgr.core.security.crypto.fromBase64
import com.nullgr.core.security.crypto.toBase64
import java.security.GeneralSecurityException
import java.security.InvalidKeyException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object AesCbcCryptonImpl {

    private const val DELIMITER = "]"
    private const val AES_CBC_PKCS7_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"

    /**
     * Encrypt given [originalText] with [secretKey] and returns result as [String].
     * For encryption used AES/CBC/PKCS7Padding transformation.
     * So given [SecretKey] has to follow this specification.
     *
     * To be sure of [secretKey], the following methods: [CryptoKeysFactory.createAESKey],
     * [CryptoKeysFactory.findOrCreateAESKey] and [CryptoKeysFactory.createAESKeyInAndroidKeystore],
     * can be used as [secretKey] parameter
     *
     * @param originalText text to be encrypted
     * @param secretKey AES [SecretKey]
     * @return [String] encrypted text
     *
     * @throws GeneralSecurityException Exceptions specified by [Cipher]
     * @throws InvalidKeyException if incorrect [SecretKey] were passed
     */
    @Throws(GeneralSecurityException::class, InvalidKeyException::class)
    fun encrypt(originalText: String, secretKey: SecretKey): String {
        return encrypt(originalText.toByteArray(), secretKey)
    }

    /**
     * Encrypt given [original] [ByteArray] with [secretKey] and returns result as [String].
     * For encryption used AES/CBC/PKCS7Padding transformation.
     * So given [SecretKey] has to follow this specification.
     *
     * To be sure of [secretKey], the following methods: [CryptoKeysFactory.createAESKey],
     * [CryptoKeysFactory.findOrCreateAESKey] and [CryptoKeysFactory.createAESKeyInAndroidKeystore],
     * can be used as [secretKey] parameter
     *
     * @param original data to be encrypted
     * @param secretKey AES [SecretKey]
     * @return [String] encrypted text
     *
     * @throws GeneralSecurityException Exceptions specified by [Cipher]
     * @throws InvalidKeyException if incorrect [SecretKey] were passed
     */
    @Throws(GeneralSecurityException::class, InvalidKeyException::class)
    fun encrypt(original: ByteArray, secretKey: SecretKey): String {
        return encryptAesCbcWithSecretKeyInternal(original, secretKey)
    }

    /**
     * Decrypt given [encryptedText] with [secretKey] and returns result as [String]
     * Encrypted text has to have transformation specified as AES/CBC/PKCS7Padding.
     * So given [SecretKey] has to follow this specification.
     *
     * @param encryptedText text to be decrypted
     * @param secretKey AES [SecretKey]
     * @return [String] decrypted text
     *
     * @throws GeneralSecurityException
     * @throws IllegalArgumentException
     */
    @Throws(GeneralSecurityException::class, IllegalArgumentException::class)
    fun decryptAsString(encryptedText: String, secretKey: SecretKey): String {
        return String(decrypt(encryptedText, secretKey))
    }

    /**
     * Decrypt given [encryptedText] with [secretKey] and returns result as [ByteArray]
     * Encrypted text has to have transformation specified as AES/CBC/PKCS7Padding.
     * So given [SecretKey] has to follow this specification.
     *
     * @param encryptedText text to be decrypted
     * @param secretKey AES [SecretKey]
     * @return [ByteArray] decrypted data
     *
     * @throws GeneralSecurityException
     * @throws IllegalArgumentException
     */
    @Throws(GeneralSecurityException::class, IllegalArgumentException::class)
    fun decrypt(encryptedText: String, secretKey: SecretKey): ByteArray {
        return decryptAesCbcWithSecretKeyInternal(encryptedText, secretKey)
    }

    private fun encryptAesCbcWithSecretKeyInternal(original: ByteArray, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(AES_CBC_PKCS7_CIPHER_ALGORITHM)
        val iv = CryptoKeysFactory.generateIv(cipher.blockSize)
        val ivParams = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams)
        val cipherBytes = cipher.doFinal(original)
        return "${iv.toBase64()}$DELIMITER${cipherBytes.toBase64()}"
    }

    private fun decryptAesCbcWithSecretKeyInternal(encryptedText: String, secretKey: SecretKey): ByteArray {
        val fields = encryptedText.split(DELIMITER)
        if (fields.size != 2) {
            throw IllegalArgumentException("Invalid encrypted text.")
        }
        val iv = fields[0].fromBase64()
        val cipherBytes = fields[1].fromBase64()
        val cipher = Cipher.getInstance(AES_CBC_PKCS7_CIPHER_ALGORITHM)
        val ivParams = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams)
        return cipher.doFinal(cipherBytes)
    }
}