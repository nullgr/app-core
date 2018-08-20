package com.nullgr.core.security.fingerprint.rx

import android.content.Context
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nullgr.core.rx.asConsumer
import com.nullgr.core.rx.asObservable
import com.nullgr.core.security.fingerprint.FingerprintAuthenticationManager
import com.nullgr.core.security.fingerprint.FingerprintResultListener
import com.nullgr.core.security.fingerprint.FingerprintView
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class RxFingerprintAuthenticationManger constructor(
    context: Context,
    successMessageDelay: Long? = null,
    resetAfterErrorDelay: Long? = null
) {
    private val viewStateRelay = BehaviorRelay.create<FingerprintViewState>()
    private val resultRelay = BehaviorRelay.create<OptionalResult<FingerprintManagerCompat.CryptoObject?>>()

    private val rxFingerprintView = object : FingerprintView {
        override fun onShowFingerprintListening() {
            viewStateRelay.asConsumer().accept(FingerprintViewState(FingerprintViewState.State.START_LISTENING))
        }

        override fun onShowAuthenticationSuccess() {
            viewStateRelay.asConsumer().accept(FingerprintViewState(FingerprintViewState.State.AUTHENTICATION_SUCCESS))
        }

        override fun onShowAuthenticationError(errorMessageId: Int?, errorMessage: String?) {
            viewStateRelay.asConsumer().accept(
                FingerprintViewState(FingerprintViewState.State.AUTHENTICATION_ERROR,
                    FingerprintViewState.Meta(errorMessageId, errorMessage))
            )
        }

        override fun onShowAuthenticationHelp(helpMessageId: Int, helpMessage: String) {
            viewStateRelay.asConsumer().accept(
                FingerprintViewState(FingerprintViewState.State.AUTHENTICATION_HELP,
                    FingerprintViewState.Meta(helpMessageId, helpMessage))
            )
        }

        override fun onResetFingerprintUiStateAfterError() {
            viewStateRelay.asConsumer().accept(FingerprintViewState(FingerprintViewState.State.RESET_AFTER_ERROR))
        }
    }

    private val fingerprintResultListener = object : FingerprintResultListener {
        override fun onSuccess(cryptoObject: FingerprintManagerCompat.CryptoObject?) {
            resultRelay.asConsumer().accept(OptionalResult(cryptoObject))
        }
    }

    private val fingerprintAuthenticationManager: FingerprintAuthenticationManager =
        FingerprintAuthenticationManager.from(context)
            .attachView(rxFingerprintView)
            .withResultListener(fingerprintResultListener)
            .resetAfterErrorDelay(resetAfterErrorDelay)
            .successResultDelay(successMessageDelay)
            .build()

    fun observeViewState(): Observable<FingerprintViewState> = viewStateRelay.asObservable()

    fun startListening(cryptoObject: FingerprintManagerCompat.CryptoObject) =
        resultRelay.asObservable()
            .doOnSubscribe {
                if (isFingerprintAuthAvailable())
                    fingerprintAuthenticationManager.startListening(cryptoObject)
                else throw IllegalStateException("Fingerprint not availabel")
            }.doOnDispose {
                fingerprintAuthenticationManager.stopListening()
            }.flatMap {
                if (!it.isEmpty) Observable.just(it.value)
                else Observable.error(SecurityException())
            }

    fun startListening() =
        resultRelay.asObservable()
            .doOnSubscribe {
                fingerprintAuthenticationManager.startListening(null)
            }.doOnDispose {
                fingerprintAuthenticationManager.stopListening()
            }.flatMapCompletable {
                Completable.complete()
            }

    fun stopListening() {
        fingerprintAuthenticationManager.stopListening()
    }

    fun isFingerprintAuthAvailable() = fingerprintAuthenticationManager.isFingerprintAuthAvailable()
}