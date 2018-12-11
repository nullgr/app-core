package com.nullgr.core.security.fingerprint

/**
 * Enum class that describes current state of fingerprint hardware
 *
 * [READY] - if everything is ready to work
 *
 * [NO_HARDWARE] - if there is no fingerprint scanner on the phone
 *
 * [KEYGUARD_NOT_SECURE] - if there is no secure pattern for unlocking the phone
 * This means that phone is ready to work with fingerprint, but user have to enable this feature in phone settings.
 *
 * [NO_ENROLLED_FINGERPRINTS] - if fingerprint's authorization dosen't activated on phone.
 * This means that phone is ready to work with fingerprint, but user have to enable this feature in phone settings.
 *
 * @author Grishko Nikita
 */
enum class FingerprintStatus {
    READY, NO_HARDWARE, KEYGUARD_NOT_SECURE, NO_ENROLLED_FINGERPRINTS
}