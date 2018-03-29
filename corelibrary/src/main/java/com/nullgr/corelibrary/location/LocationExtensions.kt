package com.nullgr.corelibrary.location

import android.location.Location

/**
 * @author chernyshov.
 */
object LocationExtensions {
    val EMPTY = Location("empty")
}

fun Location.isEmpty(): Boolean {
    return this.provider == "empty"
}

fun Location?.emptyIfNull(): Location {
    return this ?: LocationExtensions.EMPTY
}
