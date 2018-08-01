package com.nullgr.core.security.crypto

import android.content.Context
import java.security.GeneralSecurityException
import java.security.InvalidKeyException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


/**
 * Simple class to encrypt/decrypt user data with few common algorithms.
 *
 * Available encryption:
 * - Password based AES/CBC/PKCS7Padding encryption/decryption
 * - SecretKey based AES/CBC/PKCS7Padding encryption/decryption (For generation of AES key, [CryptoKeysFactory] can be used)
 * - RSA based encryption/decryption
 *
 * Note. This is an open source project, and anyone can see this code,
 * so if you really need bullet-proof encryption,
 * It is better to use your own encryption-decryption mechanisms or
 * libraries provided by companies specializing in security.
 *
 * @author Grishko Nikita
 */
class Crypton(private val context: Context) {

    companion object {
        private const val AES_CBC_PKCS7_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"
        private const val DELIMITER = "]"
    }

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