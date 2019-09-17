package com.nullgr.core.security.fingerprint

import android.content.Context
import android.os.Handler
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.nullgr.core.security.fingerprint.utils.isKeyguardSecure

/**
 * A class that provides simple and useful API to work with fingerprint.
 *
 * Simple usage:
 * ```
 * //Step 1: create new instance
 * fingerprintAuthenticationManager = FingerprintAuthenticationManager
 *      .from(it)
 *      .successResultDelay(1000) // delay time in milliseconds before call onSuccess() callback after authorization complete
 *      .resetAfterErrorDelay(1000) //delay time in milliseconds before restore UI after showing error.
 *      .attachView(fingerprintView) // Implementation of FingerprintView interface
 *      .withResultListener(object : FingerprintResultListener {
 *              override fun onSuccess(cryptoObject: FingerprintManagerCompat.CryptoObject?) {
 *                  // implement reaction for success fingerprint authorization
 *              }
 *      })
 *      .build()
 * //Step 2: Check current status of fingerprint hardware
 * fingerprintAuthenticationManager.checkFingerprintStatus()
 *
 * //Step 3: start listening. Generally you should call this in onResume().
 *
 * fingerprintAuthenticationManager.startListening(cryptoObject)
 * // if you have no need to authorize FingerprintMangerCompat.CryptoObject, you can call this method without arguments
 *
 * //Step 4: Don't forget to stop listening. Generally you should call this in onPause().
 *
 * fingerprintAuthenticationManager.stopListening()
 * ```
 *
 * @author Grishko Nikita
 */
open class FingerprintAuthenticationManager protected constructor(
    private val context: Context,
    private val fingerprintManagerCompat: FingerprintManagerCompat,
    private val view: FingerprintView,
    private val resultListener: FingerprintResultListener,
    private val successMessageDelay: Long,
    private var resetAfterErrorDelay: Long
) {

    companion object {
        private const val DEFAULT_RESET_AFTER_ERROR_DELAY_MILLIS: Long = 1600
        private const val DEFAULT_SUCCESS_DELAY_MILLIS: Long = 600

        fun from(context: Context): Builder {
            return Builder(context)
        }
    }

    private var cancellationSignal: CancellationSignal? = null
    private var selfCancelled: Boolean = false
    private val handler by lazy { Handler() }
    private val resetAfterErrorRunnable = Runnable { view.onResetFingerprintUiStateAfterError() }
    private val authenticationCallback = object : FingerprintManagerCompat.AuthenticationCallback() {

        override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
            if (selfCancelled) return
            view.onShowAuthenticationError(errMsgId, errString?.toString())
            handler.removeCallbacks(resetAfterErrorRunnable)
            handler.postDelayed(resetAfterErrorRunnable, resetAfterErrorDelay)
        }

        override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
            view.onShowAuthenticationSuccess()
            handler.postDelayed({
                resultListener.onSuccess(result?.cryptoObject)
            }, successMessageDelay)
        }

        override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
            if (selfCancelled) return
            view.onShowAuthenticationHelp(helpMsgId, helpString.toString())
            handler.removeCallbacks(resetAfterErrorRunnable)
            handler.postDelayed(resetAfterErrorRunnable, resetAfterErrorDelay)
        }

        override fun onAuthenticationFailed() {
            if (selfCancelled) return
            view.onShowAuthenticationError()
            handler.removeCallbacks(resetAfterErrorRunnable)
            handler.postDelayed(resetAfterErrorRunnable, resetAfterErrorDelay)
        }
    }

    /**
     * Check current status of fingerprint hardware.
     * @return [FingerprintStatus]
     */
    fun checkFingerprintStatus() = when {
        !fingerprintManagerCompat.isHardwareDetected -> FingerprintStatus.NO_HARDWARE
        !context.isKeyguardSecure() -> FingerprintStatus.KEYGUARD_NOT_SECURE
        !fingerprintManagerCompat.hasEnrolledFingerprints() -> FingerprintStatus.NO_ENROLLED_FINGERPRINTS
        else -> FingerprintStatus.READY
    }

    /**
     * Request authentication of a crypto object. This call warms up the fingerprint hardware
     * and starts scanning for a fingerprint.
     * @param cryptoObject instance of [FingerprintManagerCompat.CryptoObject] or null.
     */
    fun startListening(cryptoObject: FingerprintManagerCompat.CryptoObject? = null) {
        if (checkFingerprintStatus() != FingerprintStatus.READY) {
            return
        }
        cancellationSignal = CancellationSignal()
        selfCancelled = false
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, authenticationCallback, null)
        view.onShowFingerprintListening()
    }

    /**
     * Stops scanning for fingerprint.
     */
    fun stopListening() {
        if (cancellationSignal != null) {
            selfCancelled = true
            cancellationSignal!!.cancel()
            cancellationSignal = null
        }
    }

    class Builder(private val context: Context) {
        private val fingerprintManagerCompat: FingerprintManagerCompat = FingerprintManagerCompat.from(context)
        private var view: FingerprintView? = null
        private var resultListener: FingerprintResultListener? = null
        private var successMessageDelay: Long? = null
        private var resetAfterErrorDelay: Long? = null

        fun attachView(view: FingerprintView): Builder {
            this.view = view
            return this
        }

        fun withResultListener(resultListener: FingerprintResultListener): Builder {
            this.resultListener = resultListener
            return this
        }

        fun successResultDelay(delay: Long?): Builder {
            this.successMessageDelay = delay
            return this
        }

        fun resetAfterErrorDelay(delay: Long?): Builder {
            this.resetAfterErrorDelay = delay
            return this
        }

        fun build(): FingerprintAuthenticationManager {
            if (view == null) throw IllegalStateException("FingerprintView dose not initialized")
            if (resultListener == null) throw IllegalStateException("FingerprintResultListener dose not initialized")

            return FingerprintAuthenticationManager(
                context,
                fingerprintManagerCompat,
                view!!,
                resultListener!!,
                successMessageDelay ?: DEFAULT_SUCCESS_DELAY_MILLIS,
                resetAfterErrorDelay ?: DEFAULT_RESET_AFTER_ERROR_DELAY_MILLIS
            )
        }
    }
}