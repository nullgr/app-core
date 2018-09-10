package com.nullgr.core.security.prefs

import android.content.Context
import android.content.SharedPreferences
import com.nullgr.core.preferences.clear
import com.nullgr.core.preferences.edit
import com.nullgr.core.preferences.remove
import com.nullgr.core.security.prefs.crypto.PreferencesCrypton

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class CryptoPreferences(private val context: Context,
                        private val keyAlias: String,
                        private val preferencesFileName: String? = null) {

    companion object {
        private const val DEFAULT_INNER_PREF_NAME = "CryptoPreferences_Inner_Default"
    }

    private val innerPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(preferencesFileName
            ?: DEFAULT_INNER_PREF_NAME, Context.MODE_PRIVATE)
    }

    private val prefsCrypton by lazy { PreferencesCrypton.from(context, keyAlias) }

    operator fun contains(key: String): Boolean {
        return innerPreferences.contains(key)
    }

    fun setString(key: String, value: String?) = setValue(key, value)

    fun getString(key: String, defaultValue: String?) = getValue(key, defaultValue)

    fun getInt(key: String, defaultValue: Int?): Int? {
        return try {
            getValue(key, defaultValue.toString())?.toInt() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    fun setInt(key: String, value: Int) {
        setValue(key, value.toString())
    }

    fun getLong(key: String, defaultValue: Long?): Long? {
        return try {
            getValue(key, defaultValue.toString())?.toLong() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    fun setLong(key: String, value: Long) {
        setValue(key, value.toString())
    }

    fun getDouble(key: String, defaultValue: Double?): Double? {
        return try {
            getValue(key, defaultValue.toString())?.toDouble() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    fun setDouble(key: String, value: Double) {
        setValue(key, value.toString())
    }

    fun getFloat(key: String, defaultValue: Float?): Float? {
        return try {
            getValue(key, defaultValue.toString())?.toFloat() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    fun setFloat(key: String, value: Float) {
        setValue(key, value.toString())
    }

    fun getBoolean(key: String, defaultValue: Boolean?): Boolean? {
        return try {
            getValue(key, defaultValue.toString())?.toBoolean() ?: defaultValue
        } catch (err: Throwable) {
            defaultValue
        }
    }

    fun setBoolean(key: String, value: Boolean) {
        setValue(key, value.toString())
    }

    fun reset() {
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
            reset()
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
            reset()
        }
        return defaultValue
    }
}