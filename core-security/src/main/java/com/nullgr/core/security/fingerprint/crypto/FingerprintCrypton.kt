package com.nullgr.core.security.fingerprint.crypto

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyPermanentlyInvalidatedException
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.nullgr.core.security.crypto.CryptoKeysFactory
import com.nullgr.core.security.crypto.Crypton
import com.nullgr.core.security.crypto.fromBase64
import com.nullgr.core.security.crypto.internal.RsaCryptonImpl
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.UnrecoverableKeyException
import java.security.cert.CertificateException
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

/**
 * Class that provides simple functionality of encryption/decryption that requires user authorization with
 * fingerprint.
 *
 * @author Grishko Nikita
 */
object FingerprintCrypton {

    /**
     * Encrypt text with RSA key that requires user authorization for decryption.
     * Key will be generated with [CryptoKeysFactory.findOrCreateRSAKeyPairUserAuthRequired]
     *
     * @param alias       - alias of the key in [KeyStore]
     * @param initialText - text to be encrypted
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun encrypt(alias: String, initialText: String): String? {
        val publicKey = CryptoKeysFactory.findOrCreateRSAKeyPairUserAuthRequired(alias).public
        val unrestrictedPublicKey = KeyFactory.getInstance(publicKey.algorithm)
            .generatePublic(X509EncodedKeySpec(publicKey.encoded))
        return Crypton.rsaEncryption().encrypt(initialText, unrestrictedPublicKey)
    }

    /**
     * Prepare instance of [FingerprintManagerCompat.CryptoObject] with [Cipher] inside to be authorized by user.
     * You need to pass this object
     * to [com.nullgr.core.security.fingerprint.FingerprintAuthenticationManager.startListening] or
     * [com.nullgr.core.security.fingerprint.rx.RxFingerprintAuthenticationManger.startListening].
     *
     * @param alias - alias of the key in [KeyStore]
     * @throws KeyPermanentlyInvalidatedException - this exception should be handled manually in code.
     * (For example, a dialogue should be shown about the need to re-enable authorization using a fingerprint)
     * This exception will be caused if the user changes something in the security settings of the device (adds a new fingerprint, etc.)
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Throws(
        KeyPermanentlyInvalidatedException::class,
        IOException::class,
        CertificateException::class,
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidAlgorithmParameterException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class
    )
    fun prepareCryptoObject(alias: String): FingerprintManagerCompat.CryptoObject {
        val privateKey = CryptoKeysFactory.findOrCreateRSAKeyPairUserAuthRequired(alias).private
        val cipher = Cipher.getInstance(RsaCryptonImpl.RSA_CIPHER_ALGORITHM)
        try {
            cipher.init(Cipher.DECRYPT_MODE,
                privateKey,
                OAEPParameterSpec(RsaCryptonImpl.OAEP_PARAM_MD_NAME, RsaCryptonImpl.OAEP_PARAM_MGF_NAME, MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT))
        } catch (e: KeyPermanentlyInvalidatedException) {
            CryptoKeysFactory.deleteKeyFromKeyStore(alias)
            throw KeyPermanentlyInvalidatedException()
        }
        return FingerprintManagerCompat.CryptoObject(cipher)
    }

    fun prepareCryptoObjectSafe(alias: String, errorHandlingFunction: (exception: Exception) -> Unit): FingerprintManagerCompat.CryptoObject? {
        return try {
            prepareCryptoObject(alias)
        } catch (exception: Exception) {
            errorHandlingFunction.invoke(exception)
            null
        }
    }

    /**
     * Decrypt with authorized [FingerprintManagerCompat.CryptoObject] with [Cipher] inside
     *
     * @param cryptoObject - authorized by user instance of [FingerprintManagerCompat.CryptoObject]
     * @param cipherText - text to be decrypted.
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun decrypt(cryptoObject: FingerprintManagerCompat.CryptoObject, cipherText: String): String? {
        return cryptoObject.cipher?.doFinal(cipherText.fromBase64())?.let { String(it) }
    }
}