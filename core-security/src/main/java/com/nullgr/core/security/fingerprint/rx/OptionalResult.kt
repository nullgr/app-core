package com.nullgr.core.security.fingerprint.rx

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal class OptionalResult<T> constructor(val value: T?) {

    val isEmpty: Boolean
        get() = value == null

}