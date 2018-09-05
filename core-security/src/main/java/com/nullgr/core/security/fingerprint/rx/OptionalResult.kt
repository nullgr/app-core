package com.nullgr.core.security.fingerprint.rx

/**
 * Utility class to pass nullable result throw the [com.jakewharton.rxrelay2.Relay].
 * @author Grishko Nikita
 */
internal class OptionalResult<T> constructor(val value: T?) {

    val isEmpty: Boolean
        get() = value == null

}