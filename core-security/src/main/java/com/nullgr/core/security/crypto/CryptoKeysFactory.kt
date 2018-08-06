package com.nullgr.core.security.crypto

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import java.math.BigInteger
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidParameterException
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.security.auth.x500.X500Principal

/**
 * This class provides the functionality for generating various commonly used encryption keys.
 * Note that this class dosen't provide a full list of encryption keys, and generally designed for
 * classes inside this module
 *
 * @author Grishko Nikita
 */
object CryptoKeysFactory {
    private const val KEY_LENGTH = 256
    private const val KEY_LENGTH_AES = 192
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val AES_ALGORITHM = "AES"
    private const val RSA_ALGORITHM = "RSA"
    private const val X500_PRINCIPAL_NAME_MASK = "CN=%s, OU=%s"
    private const val ITERATION_COUNT = 1000
    private const val PKCS12_DERIVATION_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC"
    private const val PKCS5_SALT_LENGTH = 8
    private val secureRandom by lazy { SecureRandom() }

    /**
     * Provides a secret (symmetric) key, depending on given [algorithm] and [keyLength]
     * For full list of supported algorithm read [KeyGenerator] documentation.
     * @return Newly created [SecretKey]
     * @throws NoSuchAlgorithmException when incorrect algorithm is passed as parameter
     * @throws InvalidParameterException when passed incorrect key length for given [algorithm]
     */
    @Throws(NoSuchAlgorithmException::class, InvalidParameterException::class)
    fun createSecreteKey(algorithm: String, keyLength: Int): SecretKey {
        val kg = KeyGenerator.getInstance(algorithm)
        kg.init(keyLength)
        return kg.generateKey()
    }

    /**
     * Provides AES [SecretKey] with ***192*** key length
     * @return Newly created [SecretKey]
     */
    @Throws(NoSuchAlgorithmException::class)
    fun createAESKey() = createSecreteKey(AES_ALGORITHM, KEY_LENGTH_AES)

    /**
     * Provides password-based [SecretKey]
     * @param salt [ByteArray], for secure generation [CryptoKeysFactory.generateSalt] can be used
     * @return Newly created [SecretKey]
     */
    @Throws(InvalidKeySpecException::class)
    fun createPKCS12Key(salt: ByteArray, password: String): SecretKey {
        val keySpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val keyFactory = SecretKeyFactory.getInstance(PKCS12_DERIVATION_ALGORITHM)
        return keyFactory.generateSecret(keySpec)
    }

    /**
     * Creates RSA key pair in AndroidKeyStore with given alias
     * This method required android [Build.VERSION_CODES.KITKAT]
     * @param ctx [Context]
     * @param keyAlias alias for RSA key pair in AndroidKeyStore
     *
     * @return RSA [KeyPair]
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Throws(IllegalArgumentException::class,
        InvalidAlgorithmParameterException::class,
        NoSuchProviderException::class,
        NoSuchAlgorithmException::class)
    fun createRSAKeyPair(ctx: Context, keyAlias: String): KeyPair {
        val notBefore = Calendar.getInstance()
        val notAfter = Calendar.getInstance()
        notAfter.add(Calendar.YEAR, 20)

        val spec = KeyPairGeneratorSpec.Builder(ctx)
            .setAlias(keyAlias)
            .setSubject(X500Principal(String.format(X500_PRINCIPAL_NAME_MASK, keyAlias, ctx.packageName)))
            .setSerialNumber(BigInteger.ONE)
            .setStartDate(notBefore.time)
            .setEndDate(notAfter.time)
            .build()

        val kpGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM, ANDROID_KEY_STORE)
        kpGenerator.initialize(spec)
        return kpGenerator.generateKeyPair()
    }

    /**
     * Creates RSA key pair that requires user authorization in AndroidKeyStore with given alias
     * This method required android [Build.VERSION_CODES.M]
     * @param keyAlias alias for RSA key pair in AndroidKeyStore
     *
     * @return RSA [KeyPair]
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun createRSAKeyPairUserAuthRequired(keyAlias: String): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE)
        keyPairGenerator.initialize(
            KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .build()
        )
        return keyPairGenerator.generateKeyPair()
    }

    /**
     * Creates AES [SecretKey] in AndroidKeyStore with given alias
     * This method required android [Build.VERSION_CODES.M]
     * @param keyAlias alias for AES key in AndroidKeyStore
     *
     * @return AES [SecretKey]
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun createAESKeyInAndroidKeystore(keyAlias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(false)
                .build()
        )
        return keyGenerator.generateKey()
    }

    /**
     * Creates new RSA key pair in AndroidKeyStore with given alias or find it in keystore if it already exist.
     * This method required android [Build.VERSION_CODES.KITKAT]
     * @param ctx [Context]
     * @param keyAlias alias for RSA key pair in AndroidKeyStore
     *
     * @return RSA [KeyPair]
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun findOrCreateRSAKeyPair(ctx: Context, keyAlias: String): KeyPair {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
        if (!keyStore.containsAlias(keyAlias)) {
            createRSAKeyPair(ctx, keyAlias)
        }
        val entry = keyStore.getEntry(keyAlias, null) as KeyStore.PrivateKeyEntry
        return KeyPair(entry.certificate.publicKey, entry.privateKey)
    }

    /**
     * Creates new RSA key pair that requires user authorization in AndroidKeyStore with given alias
     * or find it in keystore if it already exist.
     * This method required android [Build.VERSION_CODES.M]
     * @param keyAlias alias for RSA key pair in AndroidKeyStore
     *
     * @return RSA [KeyPair]
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun findOrCreateRSAKeyPairUserAuthRequired(keyAlias: String): KeyPair {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
        if (!keyStore.containsAlias(keyAlias)) {
            createRSAKeyPairUserAuthRequired(keyAlias)
        }
        val entry = keyStore.getEntry(keyAlias, null) as KeyStore.PrivateKeyEntry
        return KeyPair(entry.certificate.publicKey, entry.privateKey)
    }

    /**
     * Creates AES [SecretKey] in AndroidKeyStore with given alias or find it in keystore if it already exist.
     * This method required android [Build.VERSION_CODES.M]
     * @param keyAlias alias for AES key in AndroidKeyStore
     *
     * @return AES [SecretKey]
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun findOrCreateAESKey(keyAlias: String): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
        if (!keyStore.containsAlias(keyAlias)) {
            createAESKeyInAndroidKeystore(keyAlias)
        }
        return keyStore.getKey(keyAlias, null) as SecretKey
    }

    /**
     * Generates Initialization Vector with given [length]
     * @return [ByteArray]
     */
    fun generateIv(length: Int): ByteArray {
        val array = ByteArray(length)
        secureRandom.nextBytes(array)
        return array
    }

    /**
     * Generates salt
     * @return [ByteArray]
     */
    fun generateSalt(): ByteArray {
        val b = ByteArray(PKCS5_SALT_LENGTH)
        secureRandom.nextBytes(b)
        return b
    }
}


