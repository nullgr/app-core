package com.nullgr.core.security.crypto

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import java.security.GeneralSecurityException
import java.security.InvalidKeyException
import java.security.KeyPair
import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource


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

    private const val AES_CBC_PKCS7_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding"
    private const val RSA_CIPHER_ALGORITHM = "RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING"
    private const val OAEP_PARAM_MD_NAME = "SHA-512"
    private const val OAEP_PARAM_MGF_NAME = "MGF1"
    private const val DELIMITER = "]"

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

    /**
     * Encrypt given [originalText] with RSA [keyPair] and returns result as [String].
     * For encryption used RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING transformation.
     * So given [KeyPair] has to follow this specification.
     *
     * To be sure of [keyPair], the following methods: [CryptoKeysFactory.createRSAKeyPair],
     * [CryptoKeysFactory.findOrCreateRSAKeyPair] can be used as [keyPair] parameter
     *
     * @param originalText text to be encrypted
     * @param keyPair RSA [KeyPair]
     * @return [String] encrypted text
     *
     * @throws GeneralSecurityException Exceptions specified by [Cipher]
     * @throws InvalidKeyException if incorrect [KeyPair] were passed
     */
    @Throws(GeneralSecurityException::class, InvalidKeyException::class)
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun encryptRsa(originalText: String, keyPair: KeyPair): String {
        return encryptRsa(originalText.toByteArray(), keyPair)
    }

    /**
     * Encrypt given [original] data with RSA [keyPair] and returns result as [String].
     * For encryption used RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING transformation.
     * So given [KeyPair] has to follow this specification.
     *
     * To be sure of [keyPair], the following methods: [CryptoKeysFactory.createRSAKeyPair],
     * [CryptoKeysFactory.findOrCreateRSAKeyPair] can be used as [keyPair] parameter
     *
     * @param original data to be encrypted
     * @param keyPair RSA [KeyPair]
     * @return [String] encrypted text
     *
     * @throws GeneralSecurityException Exceptions specified by [Cipher]
     * @throws InvalidKeyException if incorrect [KeyPair] were passed
     */
    @Throws(GeneralSecurityException::class, InvalidKeyException::class)
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun encryptRsa(original: ByteArray, keyPair: KeyPair): String {
        return encryptWithRSAKeyInternal(original, keyPair)
    }

    /**
     * Encrypt given [originalText] with RSA [KeyPair],
     * which will be created or found if it exists in AndroidKeyStore with [keyAlias],
     * and returns result as [String].
     * For encryption used RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING transformation.
     *
     * @param originalText text to be encrypted
     * @param keyAlias [String] key alias which will be used for key identification in AndroidKeyStore
     * @param context [Context] which will be used for [KeyPair] generation
     * @return [String] encrypted text
     *
     * @throws GeneralSecurityException Exceptions specified by [Cipher]
     * @throws InvalidKeyException if incorrect [KeyPair] were passed
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Throws(GeneralSecurityException::class, InvalidKeyException::class)
    fun encryptRsa(context: Context, originalText: String, keyAlias: String): String {
        return encryptRsa(originalText.toByteArray(), CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias));
    }

    /**
     * Encrypt given [original] data with RSA [KeyPair],
     * which will be created or found if it exists in AndroidKeyStore with [keyAlias],
     * and returns result as [String].
     * For encryption used RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING transformation.
     *
     * @param original data to be encrypted
     * @param keyAlias [String] key alias which will be used for key identification in AndroidKeyStore
     * @param context [Context] which will be used for [KeyPair] generation
     * @return [String] encrypted text
     *
     * @throws GeneralSecurityException Exceptions specified by [Cipher]
     * @throws InvalidKeyException if incorrect [KeyPair] were passed
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Throws(GeneralSecurityException::class, InvalidKeyException::class)
    fun encryptRsa(context: Context, original: ByteArray, keyAlias: String): String {
        return encryptRsa(original, CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias))
    }

    /**
     * Decrypt given [encryptedText] with RSA [keyPair] and returns result as [String].
     * For encryption used RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING transformation.
     * So given [KeyPair] has to follow this specification.
     *
     * To be sure of [keyPair], the following methods: [CryptoKeysFactory.createRSAKeyPair],
     * [CryptoKeysFactory.findOrCreateRSAKeyPair] can be used as [keyPair] parameter
     *
     * @param encryptedText text to be decrypted
     * @param keyPair RSA [KeyPair]
     * @return [String] decrypted text
     *
     * @throws GeneralSecurityException
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Throws(GeneralSecurityException::class)
    fun decryptRsaAsString(encryptedText: String, keyPair: KeyPair): String {
        return String(decryptRsa(encryptedText, keyPair))
    }

    /**
     * Decrypt given [encryptedText] with RSA [keyPair] and returns result as [ByteArray].
     * For encryption used RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING transformation.
     * So given [KeyPair] has to follow this specification.
     *
     * To be sure of [keyPair], the following methods: [CryptoKeysFactory.createRSAKeyPair],
     * [CryptoKeysFactory.findOrCreateRSAKeyPair] can be used as [keyPair] parameter
     *
     * @param encryptedText text to be decrypted
     * @param keyPair RSA [KeyPair]
     * @return [ByteArray] decrypted data
     *
     * @throws GeneralSecurityException
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Throws(GeneralSecurityException::class)
    fun decryptRsa(encryptedText: String, keyPair: KeyPair): ByteArray {
        return decryptWithRSAKeyInternal(encryptedText, keyPair)
    }

    /**
     * Decrypt given [encryptedText] with RSA [KeyPair],
     * which will be created or found if it exists in AndroidKeyStore with [keyAlias],
     * and returns result as [String].
     * For encryption used RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING transformation.
     *
     * @param encryptedText text to be decrypted
     * @param keyAlias [String] key alias which will be used for key identification in AndroidKeyStore
     * @return [String] decrypted text
     *
     * @throws GeneralSecurityException
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Throws(GeneralSecurityException::class)
    fun decryptRsaAsString(context: Context, encryptedText: String, keyAlias: String): String {
        return decryptRsaAsString(encryptedText, CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias))
    }

    /**
     * Decrypt given [encryptedText] with RSA [KeyPair],
     * which will be created or found if it exists in AndroidKeyStore with [keyAlias],
     * and returns result as [String].
     * For encryption used RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING transformation
     *
     * @param encryptedText text to be decrypted
     * @param keyAlias [String] key alias which will be used for key identification in AndroidKeyStore
     * @return [ByteArray] decrypted data
     *
     * @throws GeneralSecurityException
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Throws(GeneralSecurityException::class)
    fun decryptRsa(context: Context, encryptedText: String, keyAlias: String): ByteArray {
        return decryptRsa(encryptedText, CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias))
    }

    // ---------- Internal functions ---------- //

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

    private fun encryptWithRSAKeyInternal(original: ByteArray, keyPair: KeyPair): String {
        val cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            keyPair.public,
            OAEPParameterSpec(OAEP_PARAM_MD_NAME, OAEP_PARAM_MGF_NAME, MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT)
        )
        val cipherBytes = cipher.doFinal(original)
        return cipherBytes.toBase64()
    }

    private fun decryptWithRSAKeyInternal(encryptedText: String, keyPair: KeyPair): ByteArray {
        val cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM)
        cipher.init(
            Cipher.DECRYPT_MODE,
            keyPair.private,
            OAEPParameterSpec(OAEP_PARAM_MD_NAME, OAEP_PARAM_MGF_NAME, MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT)
        )
        return cipher.doFinal(encryptedText.fromBase64())
    }
}