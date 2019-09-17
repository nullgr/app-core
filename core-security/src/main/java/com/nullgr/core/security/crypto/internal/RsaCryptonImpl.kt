package com.nullgr.core.security.crypto.internal

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.nullgr.core.security.crypto.CryptoKeysFactory
import com.nullgr.core.security.crypto.fromBase64
import com.nullgr.core.security.crypto.toBase64
import java.security.GeneralSecurityException
import java.security.InvalidKeyException
import java.security.KeyPair
import java.security.PublicKey
import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

object RsaCryptonImpl {

    const val RSA_CIPHER_ALGORITHM = "RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING"
    const val OAEP_PARAM_MD_NAME = "SHA-512"
    const val OAEP_PARAM_MGF_NAME = "MGF1"

    /**
     * Encrypt given [originalText] with RSA [PublicKey] [publicKey] and returns result as [String].
     * For encryption used RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING transformation.
     * So given [KeyPair]'s [PublicKey] has to follow this specification.
     *
     * @param originalText text to be encrypted
     * @param publicKey RSA [PublicKey]
     * @return [String] encrypted text
     *
     * @throws GeneralSecurityException Exceptions specified by [Cipher]
     * @throws InvalidKeyException if incorrect [KeyPair] were passed
     */
    @Throws(GeneralSecurityException::class, InvalidKeyException::class)
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun encrypt(originalText: String, publicKey: PublicKey): String {
        return encryptWithRSAKeyInternal(originalText.toByteArray(), publicKey)
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
    fun encrypt(originalText: String, keyPair: KeyPair): String {
        return encrypt(originalText.toByteArray(), keyPair)
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
    fun encrypt(original: ByteArray, keyPair: KeyPair): String {
        return encryptWithRSAKeyInternal(original, keyPair.public)
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
    fun encrypt(context: Context, originalText: String, keyAlias: String): String {
        return encrypt(originalText.toByteArray(), CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias));
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
    fun encrypt(context: Context, original: ByteArray, keyAlias: String): String {
        return encrypt(original, CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias))
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
    fun decryptAsString(encryptedText: String, keyPair: KeyPair): String {
        return String(decrypt(encryptedText, keyPair))
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
    fun decrypt(encryptedText: String, keyPair: KeyPair): ByteArray {
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
    fun decryptAsString(context: Context, encryptedText: String, keyAlias: String): String {
        return decryptAsString(encryptedText, CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias))
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
    fun decrypt(context: Context, encryptedText: String, keyAlias: String): ByteArray {
        return decrypt(encryptedText, CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias))
    }

    private fun encryptWithRSAKeyInternal(original: ByteArray, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance(RSA_CIPHER_ALGORITHM)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            publicKey,
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