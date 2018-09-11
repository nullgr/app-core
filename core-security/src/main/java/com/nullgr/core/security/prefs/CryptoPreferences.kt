package com.nullgr.core.security.prefs

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.nullgr.core.preferences.clear
import com.nullgr.core.preferences.edit
import com.nullgr.core.preferences.remove
import com.nullgr.core.security.prefs.crypto.PreferencesCrypton

/**
 * Secure version of [SharedPreferences].
 * This class used to safe storing of secure needed information in simple [SharedPreferences].
 * All you need is to provide a unique [keyAlias].
 * There are several important things to know about:
 * - For android versions before [Build.VERSION_CODES.JELLY_BEAN_MR2] its not bulletproof secure.
 * - If something went wrong with encryption/decryption process - everything will be cleared and
 * encryption settings will be renewed. First of all, this behavior avoids the theft of information.
 * Also its a backdoor if something went wrong with a key in Android Keystore (expired, etc.).
 * You have to keep it in mind, to avoids unpredictable behaviour of your application.
 *
 * @property context [Context]
 * @property keyAlias Alias for encryption key.
 * The key specification dependent on current SDK version
 * @property preferencesFileName custom name for [SharedPreferences]'s file name
 *
 * @author Grishko Nikita
 */
class CryptoPreferences(private val context: Context,
                        private val keyAlias: String,
                        private val preferencesFileName: String? = null) {

    companion object {
        private const val DEFAULT_INNER_PREF_NAME = "-crypto-preferences"
    }

    private val innerPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(preferencesFileName
            ?: "${context.packageName}$DEFAULT_INNER_PREF_NAME", Context.MODE_PRIVATE)
    }

    private val prefsCrypton by lazy { PreferencesCrypton.from(context, keyAlias) }

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key The name of the preference to check.
     * @return Returns true if the preference exists in the preferences,
     *         otherwise false.
     */
    operator fun contains(key: String): Boolean {
        return innerPreferences.contains(key)
    }

    /**
     * Set an encrypted String value in the preferences.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.  Passing {@code null}
     *    for this argument is equivalent to calling {@link #remove(String)} with
     *    this key.
     */
    fun setString(key: String, value: String?) = setValue(key, value)

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.
     */
    fun getString(key: String, defaultValue: String?) = getValue(key, defaultValue)

    /**
     * Retrieve an Integer value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.
     */
    fun getInt(key: String, defaultValue: Int?): Int? {
        return try {
            getValue(key, defaultValue.toString())?.toInt() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    /**
     * Set an encrypted Int value in the preferences.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.  Passing {@code null}
     *    for this argument is equivalent to calling {@link #remove(String)} with
     *    this key.
     */
    fun setInt(key: String, value: Int) {
        setValue(key, value.toString())
    }

    /**
     * Retrieve a Long value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.
     */
    fun getLong(key: String, defaultValue: Long?): Long? {
        return try {
            getValue(key, defaultValue.toString())?.toLong() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    /**
     * Set an encrypted Long value in the preferences.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.  Passing {@code null}
     *    for this argument is equivalent to calling {@link #remove(String)} with
     *    this key.
     */
    fun setLong(key: String, value: Long) {
        setValue(key, value.toString())
    }

    /**
     * Retrieve a Double value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.
     */
    fun getDouble(key: String, defaultValue: Double?): Double? {
        return try {
            getValue(key, defaultValue.toString())?.toDouble() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    /**
     * Set an encrypted Double value in the preferences.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.  Passing {@code null}
     *    for this argument is equivalent to calling {@link #remove(String)} with
     *    this key.
     */
    fun setDouble(key: String, value: Double) {
        setValue(key, value.toString())
    }

    /**
     * Retrieve a Float value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.
     */
    fun getFloat(key: String, defaultValue: Float?): Float? {
        return try {
            getValue(key, defaultValue.toString())?.toFloat() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    /**
     * Set an encrypted Float value in the preferences.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.  Passing {@code null}
     *    for this argument is equivalent to calling {@link #remove(String)} with
     *    this key.
     */
    fun setFloat(key: String, value: Float) {
        setValue(key, value.toString())
    }

    /**
     * Retrieve a Boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.
     */
    fun getBoolean(key: String, defaultValue: Boolean?): Boolean? {
        return try {
            getValue(key, defaultValue.toString())?.toBoolean() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    /**
     * Set an encrypted Boolean value in the preferences.
     *
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.  Passing {@code null}
     *    for this argument is equivalent to calling {@link #remove(String)} with
     *    this key.
     */
    fun setBoolean(key: String, value: Boolean) {
        setValue(key, value.toString())
    }

    /**
     * Drops all values from preferences and reset encryption/decryption settings
     */
    fun clear() {
        innerPreferences.clear()
        prefsCrypton.reset()
    }

    private fun setValue(key: String, value: String?) {
        try {
            if (value.isNullOrEmpty()) innerPreferences.remove(key)

            innerPreferences.edit {
                it.putString(key, prefsCrypton.encrypt(value!!))
            }
        } catch (throwable: Throwable) {
            clear()
        }
    }

    private fun getValue(key: String, defaultValue: String?): String? {
        try {
            val encryptedValue = innerPreferences.getString(key, defaultValue)
            if (encryptedValue.isNullOrEmpty() || encryptedValue.equals(defaultValue, false)) {
                return encryptedValue
            }
            return prefsCrypton.decrypt(encryptedValue)
        } catch (throwable: Throwable) {
            clear()
        }
        return defaultValue
    }
}