package com.nullgr.androidcore.fingerprint

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nullgr.androidcore.R
import com.nullgr.core.preferences.defaultPrefs
import com.nullgr.core.security.fingerprint.crypto.FingerprintCrypton
import com.nullgr.core.security.fingerprint.rx.RxFingerprintAuthenticationManger
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_fingerprint_example.buttonFingerprintDialog

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class FingerprintExampleActivity : AppCompatActivity() {

    companion object {
        private const val KEY = "preferences_key"
        private const val RSA_KEY_ALIAS ="super_cool_rsa_key"
    }

    private val rxFingerprintManger by lazy { RxFingerprintAuthenticationManger(this) }
    private val preferences by lazy { defaultPrefs(this) }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint_example)
        bindDefaultUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun bindDefaultUI() {
        when {
            !rxFingerprintManger.isFingerprintAuthAvailable() -> bindAsNotAvailable()
            preferences.contains(KEY) -> bindAsListenFingerprint()
            else -> bindAsSetupFingerprint()
        }

        buttonFingerprintDialog.setOnClickListener { }
    }

    private fun bindAsSetupFingerprint() {

    }

    private fun bindAsListenFingerprint() {

        rxFingerprintManger.observeViewState().subscribe {

        }.addTo(compositeDisposable)

        rxFingerprintManger.startListening().subscribe().addTo(compositeDisposable)
    }

    private fun bindAsNotAvailable() {

    }
}