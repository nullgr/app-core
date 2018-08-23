package com.nullgr.core.security.fingerprint.rx

import android.content.Context
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nullgr.core.rx.asConsumer
import com.nullgr.core.rx.asObservable
import com.nullgr.core.security.fingerprint.FingerprintAuthenticationManager
import com.nullgr.core.security.fingerprint.FingerprintResultListener
import com.nullgr.core.security.fingerprint.errors.FingerprintNotAvailableException
import com.nullgr.core.security.fingerprint.rx.view.FingerprintViewState
import com.nullgr.core.security.fingerprint.rx.view.RxFingerprintView
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

    private val rxFingerprintView = RxFingerprintView(viewStateRelay.asConsumer())

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

    fun isFingerprintAuthAvailable() = fingerprintAuthenticationManager.isFingerprintAuthAvailable()

    fun observeViewState(): Observable<FingerprintViewState> = viewStateRelay.asObservable()

    fun startListening(cryptoObject: FingerprintManagerCompat.CryptoObject) =
        innerPrepareListening(cryptoObject)
            .flatMap {
                if (!it.isEmpty) Observable.just(it.value)
                else Observable.error(IllegalStateException("Empty crypto object were returned"))
            }

    fun startListening() =
        innerPrepareListening()
            .flatMapCompletable {
                Completable.complete()
            }

    fun stopListening() {
        fingerprintAuthenticationManager.stopListening()
    }

    private fun innerPrepareListening(cryptoObject: FingerprintManagerCompat.CryptoObject? = null): Observable<OptionalResult<FingerprintManagerCompat.CryptoObject?>> {
        return resultRelay.asObservable()
            .doOnSubscribe {
                if (isFingerprintAuthAvailable()) fingerprintAuthenticationManager.startListening(cryptoObject)
                else throw FingerprintNotAvailableException()
            }.doOnDispose {
                fingerprintAuthenticationManager.stopListening()
            }
    }
}