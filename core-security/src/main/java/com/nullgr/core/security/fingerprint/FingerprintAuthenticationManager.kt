package com.nullgr.core.security.fingerprint

import android.content.Context
import android.os.Handler
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal
import com.nullgr.core.security.fingerprint.utils.isKeyguardSecure

/**
 * Created by Grishko Nikita on 01.02.18.
 */
open class FingerprintAuthenticationManager protected constructor(
    private val context: Context,
    private val fingerprintManagerCompat: FingerprintManagerCompat,
    private val view: FingerprintView,
    private val resultListener: FingerprintResultListener,
    private val successMessageDelay: Long,
    private var resetAfterErrorDelay: Long
) : FingerprintManagerCompat.AuthenticationCallback() {

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

    fun checkFingerprintStatus() = when {
        !fingerprintManagerCompat.isHardwareDetected -> FingerprintStatus.NO_HARDWARE
        !context.isKeyguardSecure() -> FingerprintStatus.KEYGUARD_NOT_SECURE
        !fingerprintManagerCompat.hasEnrolledFingerprints() -> FingerprintStatus.NO_ENROLLED_FINGERPRINTS
        else -> FingerprintStatus.READY
    }

    fun startListening(cryptoObject: FingerprintManagerCompat.CryptoObject? = null) {
        if (checkFingerprintStatus() != FingerprintStatus.READY) {
            return
        }
        cancellationSignal = CancellationSignal()
        selfCancelled = false
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, this, null)
        view.onShowFingerprintListening()
    }

    fun stopListening() {
        if (cancellationSignal != null) {
            selfCancelled = true
            cancellationSignal!!.cancel()
            cancellationSignal = null
        }
    }

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