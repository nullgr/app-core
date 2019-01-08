package com.nullgr.core.security.prefs.crypto

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import com.nullgr.core.preferences.clear
import com.nullgr.core.preferences.customPrefs
import com.nullgr.core.preferences.edit
import com.nullgr.core.security.crypto.CryptoKeysFactory
import com.nullgr.core.security.crypto.Crypton
import com.nullgr.core.security.crypto.fromBase64
import com.nullgr.core.security.crypto.toBase64
import com.nullgr.core.security.crypto.toHexDecimalString
import java.security.GeneralSecurityException
import java.security.InvalidKeyException
import javax.crypto.SecretKey


/**
 * This class provides the functionality of encryption/decryption for
 * [com.nullgr.core.security.prefs.CryptoPreferences] depending on current SDK version.
 * Note!!! For android versions before [Build.VERSION_CODES.JELLY_BEAN_MR2] its not bulletproof secure.
 * For versions later ANDROID KEYSTORE used as keys storage.
 *
 * @author Grishko Nikita
 */
internal abstract class PreferencesCrypton(protected val context: Context, protected val keyAlias: String) {

    companion object {
        fun from(context: Context, keyAlias: String): PreferencesCrypton {
            return when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 -> LegacyPrefsCryptonImpl(context, keyAlias)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> Api23PrefsCryptonImpl(context, keyAlias)
                else -> Api18PrefsCryptonImpl(context, keyAlias)
            }
        }
    }

    protected val internalPreferences
        by lazy { customPrefs(context, "${context.packageName}-crypton-internal-storage") }

    abstract fun encrypt(value: String): String

    abstract fun decrypt(value: String): String

    abstract fun reset()
}

internal class LegacyPrefsCryptonImpl(context: Context, keyAlias: String) : PreferencesCrypton(context, keyAlias) {

    companion object {
        private const val KEY_PART_ONE = "dHCGa.#T9ufOi(h"
    }

    override fun reset() {
        internalPreferences.clear()
    }

    @Throws(GeneralSecurityException::class)
    override fun encrypt(value: String): String = Crypton.passwordBasedEncryption().encrypt(value, getEncryptionKey())

    @Throws(GeneralSecurityException::class)
    override fun decrypt(value: String): String = Crypton.passwordBasedEncryption().decryptAsString(value, getEncryptionKey())

    private fun getEncryptionKey(): String {
        var keyPart2: String? = internalPreferences.getString(keyAlias, null)
        if (keyPart2.isNullOrEmpty()) {
            internalPreferences.edit {
                keyPart2 = CryptoKeysFactory.generateSalt().toHexDecimalString()
                it.putString(keyAlias, keyPart2)
            }
        }
        return "$KEY_PART_ONE]$keyPart2]${keyAlias.toBase64()}"
    }
}

@TargetApi(23)
internal class Api23PrefsCryptonImpl(context: Context, keyAlias: String) : PreferencesCrypton(context, keyAlias) {

    override fun reset() {
        internalPreferences.clear()
        CryptoKeysFactory.deleteKeyFromKeyStore(keyAlias)
    }

    @Throws(GeneralSecurityException::class, InvalidKeyException::class)
    override fun encrypt(value: String) = Crypton.aesCbcEncryption().encrypt(value, getEncryptionKey())

    override fun decrypt(value: String) = Crypton.aesCbcEncryption().decryptAsString(value, getEncryptionKey())

    private fun getEncryptionKey() = CryptoKeysFactory.findOrCreateAESKey(keyAlias)
}

@TargetApi(18)
internal class Api18PrefsCryptonImpl(context: Context, keyAlias: String) : PreferencesCrypton(context, keyAlias) {

    companion object {
        private const val STORED_KEY = "pref_wrapped_key"
    }

    private var keyWrapper = KeyWrapper(context, keyAlias)
    private var secretKey = getEncryptionKey()

    override fun reset() {
        internalPreferences.clear()
        keyWrapper.reset()
        secretKey = getEncryptionKey()
    }

    override fun encrypt(value: String): String = Crypton.aesCbcEncryption().encrypt(value, secretKey)

    override fun decrypt(value: String): String = Crypton.aesCbcEncryption().decryptAsString(value, secretKey)

    @Throws(GeneralSecurityException::class)
    private fun getEncryptionKey(): SecretKey {
        var wrappedKey = getWrappedKey()
        return if (wrappedKey.isNullOrEmpty()) {
            val key = CryptoKeysFactory.createAESKey()
            wrappedKey = keyWrapper.wrap(key).toBase64()
            saveWrappedKey(wrappedKey)
            key
        } else {
            keyWrapper.unwrap(wrappedKey!!.fromBase64())
        }
    }

    private fun getWrappedKey(): String? {
        return internalPreferences.getString(STORED_KEY, null)
    }

    private fun saveWrappedKey(wrappedKey: String) {
        internalPreferences.edit {
            it.putString(STORED_KEY, wrappedKey)
        }
    }
}