package com.nullgr.core.security.fingerprint.errors

import com.nullgr.core.security.fingerprint.FingerprintStatus

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class FingerprintNotAvailableException(val fingerprintStatus: FingerprintStatus) : Throwable()