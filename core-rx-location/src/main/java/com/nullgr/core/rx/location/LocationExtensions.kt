package com.nullgr.core.rx.location

import android.location.Location

/**
 * Instance of [Location] with `empty` provider.
 */
val EMPTY_LOCATION = Location("empty")

/**
 * Helper function to check is provider of this location `empty`.
 *
 * @receiver [Location] object to check.
 * @return `True` if location provider equals `empty`, false otherwise.
 */
fun Location.isEmpty(): Boolean {
    return this.provider == "empty"
}

/**
 * Helper function that returns [EMPTY_LOCATION] if this object is `null`.
 *
 * @receiver [Location] object to check.
 * @return [EMPTY_LOCATION] if this object is `null`, false otherwise.
 */
fun Location?.emptyIfNull(): Location {
    return this ?: EMPTY_LOCATION
}
