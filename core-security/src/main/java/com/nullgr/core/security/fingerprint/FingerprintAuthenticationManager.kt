package com.nullgr.core.security.fingerprint

import android.content.Context
import android.os.Handler
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal


/**
 * Created by Grishko Nikita on 01.02.18.
 */
open class FingerprintAuthenticationManager private constructor(
    private val fingerprintManagerCompat: FingerprintManagerCompat,
    private val uiProvider: FingerprintUiProvider,
    protected val resultListener: FingerprintResultListener,
    protected val successMessageDelay: Long,
    protected var errorMessageDelay: Long
) : FingerprintManagerCompat.AuthenticationCallback() {

    companion object {
        private const val DEFAULT_ERROR_TIMEOUT_MILLIS: Long = 1600
        private const val DEFAULT_SUCCESS_DELAY_MILLIS: Long = 600

        fun from(context: Context): Builder {
            return Builder(context)
        }
    }

    private var cancellationSignal: CancellationSignal? = null
    private var selfCancelled: Boolean = false
    private val handler by lazy { Handler() }
    private val resetAfterErrorRunnable = Runnable { uiProvider.resetFingerprintUiStateAfterError() }

    fun isFingerprintAuthAvailable() =
        fingerprintManagerCompat.isHardwareDetected
            && fingerprintManagerCompat.hasEnrolledFingerprints()

    fun startListening(cryptoObject: FingerprintManagerCompat.CryptoObject?) {
        if (!isFingerprintAuthAvailable()) {
            return
        }
        cancellationSignal = CancellationSignal()
        selfCancelled = false
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, this, null)
        uiProvider.showFingerprintListening()
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
        uiProvider.showAuthenticationError(errMsgId, errString?.toString())
        handler.removeCallbacks(resetAfterErrorRunnable)
        handler.postDelayed(resetAfterErrorRunnable, errorMessageDelay)
    }

    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
        uiProvider.showAuthenticationSuccess()
        handler.postDelayed({
            resultListener.onSuccess(result?.cryptoObject)
        },successMessageDelay)
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
        if (selfCancelled) return
        uiProvider.showAuthenticationHelp(helpMsgId, helpString.toString())
        handler.removeCallbacks(resetAfterErrorRunnable)
        handler.postDelayed(resetAfterErrorRunnable, errorMessageDelay)
    }

    override fun onAuthenticationFailed() {
        if (selfCancelled) return
        uiProvider.showAuthenticationError()
        handler.removeCallbacks(resetAfterErrorRunnable)
        handler.postDelayed(resetAfterErrorRunnable, errorMessageDelay)
    }

    class Builder(context: Context) {
        private val fingerprintManagerCompat: FingerprintManagerCompat = FingerprintManagerCompat.from(context)
        private var uiProvider: FingerprintUiProvider? = null
        private var resultListener: FingerprintResultListener? = null
        private var successMessageDelay: Long? = null
        private var errorMessageDelay: Long? = null

        fun withUiProvider(uiProvider: FingerprintUiProvider): Builder {
            this.uiProvider = uiProvider
            return this
        }

        fun withResultListener(resultListener: FingerprintResultListener): Builder {
            this.resultListener = resultListener
            return this
        }

        fun successResultDelay(delay: Long): Builder {
            this.successMessageDelay = delay
            return this
        }

        fun errorResultDelay(delay: Long): Builder {
            this.errorMessageDelay = delay
            return this
        }

        fun build(): FingerprintAuthenticationManager {
            if (uiProvider == null) throw IllegalStateException("FingerprintUiProvider dose not initialized")
            if (resultListener == null) throw IllegalStateException("FingerprintResultListener dose not initialized")

            return FingerprintAuthenticationManager(
                fingerprintManagerCompat,
                uiProvider!!,
                resultListener!!,
                successMessageDelay ?: DEFAULT_SUCCESS_DELAY_MILLIS,
                errorMessageDelay ?: DEFAULT_ERROR_TIMEOUT_MILLIS
            )
        }
    }
}