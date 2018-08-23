package com.nullgr.core.security.fingerprint.crypto

import android.annotation.TargetApi
import android.os.Build
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import com.nullgr.core.security.crypto.CryptoKeysFactory
import com.nullgr.core.security.crypto.Crypton
import com.nullgr.core.security.crypto.fromBase64
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
 * Created by Grishko Nikita on 01.02.18.
 * //TODO need to be tested!!!!
 */
object FingerprintCrypton {

    /**
     * Encrypt text with RSA key
     *
     * @param alias       - alias of the key in [KeyStore]
     * @param initialText - text to be encrypted
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun encrypt(alias: String, initialText: String): String? {
        val publicKey = CryptoKeysFactory.findOrCreateRSAKeyPairUserAuthRequired(alias).public
        val unrestrictedPublicKey = KeyFactory.getInstance(publicKey.algorithm)
            .generatePublic(X509EncodedKeySpec(publicKey.encoded))
        return Crypton.encryptRsa(initialText, unrestrictedPublicKey)
    }

    /**
     * Prepare instance of [Cipher] to be authorized by user/
     *
     * @param alias - alias of the key in [KeyStore]
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Throws(IOException::class, CertificateException::class, UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidAlgorithmParameterException::class, NoSuchPaddingException::class, InvalidKeyException::class)
    fun prepareCryptoObject(alias: String): FingerprintManagerCompat.CryptoObject {
        val privateKey = CryptoKeysFactory.findOrCreateRSAKeyPairUserAuthRequired(alias).private
        val cipher = Cipher.getInstance(Crypton.RSA_CIPHER_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE,
            privateKey,
            OAEPParameterSpec(Crypton.OAEP_PARAM_MD_NAME, Crypton.OAEP_PARAM_MGF_NAME, MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT))
        return FingerprintManagerCompat.CryptoObject(cipher)
    }

    /**
     * Decrypt with authorized instance of [Cipher]
     *
     * @param cipher     - authorized by user instance of [Cipher]
     * @param cipherText - text to be encrypted
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun decrypt(cryptoObject: FingerprintManagerCompat.CryptoObject, cipherText: String): String? {
        return cryptoObject.cipher?.doFinal(cipherText.fromBase64())?.let { String(it) }
    }
}