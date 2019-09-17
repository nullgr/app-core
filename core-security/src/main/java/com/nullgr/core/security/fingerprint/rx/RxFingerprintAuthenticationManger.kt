package com.nullgr.core.security.fingerprint.rx

import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.nullgr.core.rx.asConsumer
import com.nullgr.core.rx.asObservable
import com.nullgr.core.rx.relay.SingleSubscriberRelay
import com.nullgr.core.security.fingerprint.FingerprintAuthenticationManager
import com.nullgr.core.security.fingerprint.FingerprintResultListener
import com.nullgr.core.security.fingerprint.FingerprintStatus
import com.nullgr.core.security.fingerprint.errors.FingerprintNotAvailableException
import com.nullgr.core.security.fingerprint.rx.view.FingerprintViewState
import com.nullgr.core.security.fingerprint.rx.view.RxFingerprintView
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * A class that provides simple and useful API in RX way to work with fingerprint.
 *
 * Simple usage:
 * ```
 * //Step 1: Initialize val
 * val rxFingerprintManger = RxFingerprintAuthenticationManger(context)
 *
 * //Step 2: Check the status
 * rxFingerprintManger.checkFingerprintStatus()
 *
 * //Step 3: Observe changes of view state
 * rxFingerprintManger.observeViewState().subscribe {
 *      when (it.state) {
 *        //implement UI changes for each state
 *      }
 * }.addTo(compositeDisposable)
 *
 * //Step 3: start listening. It will starts automatically after subscribing
 * rxFingerprintManger.startListening(cryptoObject)
 *      .subscribe({
 *            // implement reaction on success authorization
 *      }, {
 *              showAlert(it.toString())
 *      }).addTo(compositeDisposable)
 *
 * //Step 4: Stop listening
 * compositeDisposable.clear() //Scanning will terminate automatically when subscription dispose
 * //or
 * rxFingerprintManger.stopListening()
 * ```
 *
 * @author Grishko Nikita
 */
class RxFingerprintAuthenticationManger constructor(
    context: Context,
    successMessageDelay: Long? = null,
    resetAfterErrorDelay: Long? = null
) {
    private val viewStateRelay = SingleSubscriberRelay.create<FingerprintViewState>()
    private val resultRelay = SingleSubscriberRelay.create<OptionalResult<FingerprintManagerCompat.CryptoObject?>>()

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

    /**
     * Check current status of fingerprint hardware.
     * @return [FingerprintStatus]
     */
    fun checkFingerprintStatus() = fingerprintAuthenticationManager.checkFingerprintStatus()

    /**
     * An observable that emits [FingerprintViewState]
     */
    fun observeViewState(): Observable<FingerprintViewState> = viewStateRelay.asObservable()

    /**
     * Request authentication of a crypto object. This call warms up the fingerprint hardware
     * and starts scanning for a fingerprint.
     * @param cryptoObject instance of [FingerprintManagerCompat.CryptoObject]
     * @return [Observable] that emits authorized [FingerprintManagerCompat.CryptoObject]
     */
    fun startListening(cryptoObject: FingerprintManagerCompat.CryptoObject): Observable<FingerprintManagerCompat.CryptoObject?> =
        innerPrepareListening(cryptoObject)
            .flatMap {
                if (!it.isEmpty) Observable.just(it.value)
                else Observable.error(IllegalStateException("Empty crypto object were returned"))
            }

    /**
     * This call warms up the fingerprint hardware and starts scanning for a fingerprint.
     * @return [Completable]
     */
    fun startListening(): Completable =
        innerPrepareListening()
            .flatMapCompletable {
                Completable.complete()
            }
    
    /**
     * Stops scanning for fingerprint.
     */
    fun stopListening() {
        fingerprintAuthenticationManager.stopListening()
    }

    private fun innerPrepareListening(cryptoObject: FingerprintManagerCompat.CryptoObject? = null): Observable<OptionalResult<FingerprintManagerCompat.CryptoObject?>> {
        return resultRelay.asObservable()
            .doOnSubscribe {
                val status = checkFingerprintStatus()
                if (status == FingerprintStatus.READY) fingerprintAuthenticationManager.startListening(cryptoObject)
                else throw FingerprintNotAvailableException(status)
            }.doOnDispose {
                fingerprintAuthenticationManager.stopListening()
            }
    }
}