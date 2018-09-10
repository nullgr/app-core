package com.nullgr.core.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Fabric function to create default [SharedPreferences]
 * @param context [Context]
 */
fun defaultPrefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

/**
 * Fabric function to create custom [SharedPreferences] with give [name]
 * @param context [Context]
 * @param name [SharedPreferences] name
 */
fun customPrefs(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

/**
 * Operator function to easy save, primitive types values in [SharedPreferences].
 * Supported this types: [String], [Int], [Boolean], [Float], [Long]
 * @param key [String] key for [SharedPreferences]
 * @param value value to save in [SharedPreferences].
 * Can be null. If null is passed, the entry with the specified [key] will be deleted
 * @throws UnsupportedOperationException if any other type of [value] is set
 */
operator fun SharedPreferences.set(key: String, value: Any?) {
    if (value == null) {
        edit { it.remove(key) }
    } else {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}

/**
 * Operator function to quick access for entry in [SharedPreferences].
 * Supported types: [String], [Int], [Boolean], [Float], [Long]
 * @param key [String] key for [SharedPreferences]
 * @param defaultValue value to return if this preference does not exist.
 * @throws UnsupportedOperationException
 */
inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
    return when (T::class) {
        String::class -> getString(key, defaultValue as? String) as T?
        Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
        Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
        Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

/**
 * Removes preference with give [key]
 * @param key [String] key
 */
fun SharedPreferences.remove(key: String) {
    edit { it.remove(key) }
}

/**
 * Removes ***all*** values from the preferences.
 */
fun SharedPreferences.clear() {
    edit { it.clear() }
}

/**
 * Performs [operation] with [SharedPreferences.Editor] and applys changes.
 */
inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

