package com.nullgr.core.security.fingerprint.errors

import com.nullgr.core.security.fingerprint.FingerprintStatus

/**
 * An error that will be thrown if fingerprint is not available.
 * @author Grishko Nikita
 */
class FingerprintNotAvailableException(val fingerprintStatus: FingerprintStatus) : Throwable()