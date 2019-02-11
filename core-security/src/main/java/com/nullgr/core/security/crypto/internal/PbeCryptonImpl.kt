package com.nullgr.core.security.crypto.internal

import com.nullgr.core.security.crypto.CryptoKeysFactory
import com.nullgr.core.security.crypto.fromBase64
import com.nullgr.core.security.crypto.toBase64
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object PbeCryptonImpl {

    private const val DELIMITER = "]"
    private const val AES_CBC_PKCS7_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"

    /**
     * Encrypt given [originalText] with key based on given [password]
     * and returns result as [String]
     * @param originalText text to be encrypted
     * @param password secure password
     * @return [String] encrypted text
     * @throws GeneralSecurityException Exceptions specified by [Cipher]
     */
    @Throws(GeneralSecurityException::class)
    fun encrypt(originalText: String, password: String): String {
        return encrypt(originalText.toByteArray(), password)
    }

    /**
     * Encrypt given [original] with key based on given [password]
     * and returns result as [String]
     * @param original [ByteArray] data to be encrypted
     * @param password secure password
     * @return [String] encrypted text
     * @throws GeneralSecurityException Exceptions specified by [Cipher]
     */
    @Throws(GeneralSecurityException::class)
    fun encrypt(original: ByteArray, password: String): String {
        return encryptWithPasswordInternal(original, password)
    }

    /**
     * Decrypt given [encryptedText] with key based on given [password]
     * and returns result as [String]
     *
     * @param encryptedText text to be decrypted
     * @param password secure password
     * @return [String] decrypted text
     *
     * @throws GeneralSecurityException
     * @throws IllegalArgumentException
     */
    @Throws(GeneralSecurityException::class, IllegalArgumentException::class)
    fun decryptAsString(encryptedText: String, password: String): String {
        return String(decrypt(encryptedText, password))
    }

    /**
     * Decrypt given [encryptedText] with key based on given [password]
     * and returns result as [ByteArray]
     *
     * @param encryptedText text to be decrypted
     * @param password secure password
     * @return [ByteArray] decrypted data
     *
     * @throws GeneralSecurityException
     * @throws IllegalArgumentException
     */
    fun decrypt(encryptedText: String, password: String): ByteArray {
        return decryptWithPasswordInternal(encryptedText, password)
    }

    private fun encryptWithPasswordInternal(original: ByteArray, password: String): String {
        val salt = CryptoKeysFactory.generateSalt()
        val key: SecretKey = CryptoKeysFactory.createPKCS12Key(salt, password)
        val cipher = Cipher.getInstance(AES_CBC_PKCS7_CIPHER_ALGORITHM)
        val iv = CryptoKeysFactory.generateIv(cipher.blockSize)
        val ivParams = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams)
        val cipherText = cipher.doFinal(original)
        return "${salt.toBase64()}$DELIMITER${iv.toBase64()}$DELIMITER${cipherText.toBase64()}"
    }

    private fun decryptWithPasswordInternal(encryptedText: String, password: String): ByteArray {
        val fields = encryptedText.split(DELIMITER)
        if (fields.size != 3) {
            throw IllegalArgumentException("Invalid encrypted text.")
        }
        val salt = fields[0].fromBase64()
        val iv = fields[1].fromBase64()
        val cipherBytes = fields[2].fromBase64()

        val key: SecretKey = CryptoKeysFactory.createPKCS12Key(salt, password)
        val cipher = Cipher.getInstance(AES_CBC_PKCS7_CIPHER_ALGORITHM)
        val ivParams = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, key, ivParams)
        return cipher.doFinal(cipherBytes)
    }
}