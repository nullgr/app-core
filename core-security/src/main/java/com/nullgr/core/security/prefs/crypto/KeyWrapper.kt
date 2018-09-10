package com.nullgr.core.security.prefs.crypto

import android.annotation.TargetApi
import android.content.Context
import com.nullgr.core.security.crypto.CryptoKeysFactory
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.SecretKey

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@TargetApi(18)
internal class KeyWrapper(private val context: Context,
                          private val keyAlias: String) {
    companion object {
        private const val RSA_ALGORITHM = "RSA/ECB/PKCS1Padding"
    }

    private val cipher by lazy { Cipher.getInstance(RSA_ALGORITHM) }
    private var keyPair = CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias)

    /**
     * Wrap a [javax.crypto.SecretKey] using the public key assigned to this wrapper.
     * Use [.unwrap] to later recover the original
     * [javax.crypto.SecretKey].
     *
     * @return a wrapped version of the given [javax.crypto.SecretKey] that can be
     * safely stored on untrusted storage.
     */
    @Throws(GeneralSecurityException::class)
    fun wrap(key: SecretKey): ByteArray {
        cipher.init(Cipher.WRAP_MODE, keyPair.public)
        return cipher.wrap(key)
    }

    /**
     * Unwrap a [javax.crypto.SecretKey] using the private key assigned to this
     * wrapper.
     *
     * @param blob a wrapped [javax.crypto.SecretKey] as previously returned by
     * [.wrap].
     */
    @Throws(GeneralSecurityException::class)
    fun unwrap(blob: ByteArray): SecretKey {
        cipher.init(Cipher.UNWRAP_MODE, keyPair.private)
        return cipher.unwrap(blob, "AES", Cipher.SECRET_KEY) as SecretKey
    }

    fun reset(){
        CryptoKeysFactory.deleteKeyFromKeyStore(keyAlias)
        keyPair = CryptoKeysFactory.findOrCreateRSAKeyPair(context, keyAlias)
    }
}