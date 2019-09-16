package com.nullgr.androidcore.fingerprint

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.security.keystore.KeyPermanentlyInvalidatedException
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.nullgr.androidcore.R
import com.nullgr.androidcore.fingerprint.dialog.SimpleFingerprintDialog
import com.nullgr.core.preferences.defaultPrefs
import com.nullgr.core.preferences.get
import com.nullgr.core.preferences.remove
import com.nullgr.core.preferences.set
import com.nullgr.core.security.fingerprint.FingerprintStatus
import com.nullgr.core.security.fingerprint.crypto.FingerprintCrypton
import com.nullgr.core.security.fingerprint.rx.RxFingerprintAuthenticationManger
import com.nullgr.core.security.fingerprint.rx.view.FingerprintViewState
import com.nullgr.core.ui.extensions.hide
import com.nullgr.core.ui.extensions.show
import com.nullgr.core.ui.fragments.showDialog
import com.nullgr.core.ui.toast.showToast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_fingerprint_example.buttonFingerprintDialog
import kotlinx.android.synthetic.main.activity_fingerprint_example.buttonResetFingerprint
import kotlinx.android.synthetic.main.activity_fingerprint_example.buttonSave
import kotlinx.android.synthetic.main.activity_fingerprint_example.containerFingerprint
import kotlinx.android.synthetic.main.activity_fingerprint_example.fingerprintStatusImageView
import kotlinx.android.synthetic.main.activity_fingerprint_example.secureTextInput
import kotlinx.android.synthetic.main.activity_fingerprint_example.setUpFingerprintContainer
import kotlinx.android.synthetic.main.activity_fingerprint_example.statusTextView

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class FingerprintExampleActivity : AppCompatActivity() {

    companion object {
        private const val KEY = "preferences_key"
        private const val RSA_KEY_ALIAS = "super_cool_rsa_key"
        private const val LOG_TAG = "FingerprintTag"
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
        val status = rxFingerprintManger.checkFingerprintStatus()
        when (status) {
            FingerprintStatus.KEYGUARD_NOT_SECURE, FingerprintStatus.NO_ENROLLED_FINGERPRINTS, FingerprintStatus.NO_HARDWARE -> bindAsNotAvailable(status)
            else -> {
                buttonFingerprintDialog.setOnClickListener {
                    supportFragmentManager.showDialog(SimpleFingerprintDialog.getInstance())
                }
                when {
                    preferences.contains(KEY) -> bindAsListenFingerprint()
                    else -> bindAsSetupFingerprint()
                }
            }
        }
    }

    private fun bindAsSetupFingerprint() {
        setUpFingerprintContainer.show()
        containerFingerprint.hide()
        buttonSave.setOnClickListener {
            if (!secureTextInput.text.isNullOrEmpty()) {
                val encryptedText = FingerprintCrypton.encrypt(RSA_KEY_ALIAS, secureTextInput.text.toString())
                Log.d(LOG_TAG, encryptedText)
                preferences[KEY] = encryptedText
                getString(R.string.title_saved_success).showToast(this)
                bindAsListenFingerprint()
            }
        }
    }

    @SuppressLint("NewApi")
    private fun bindAsListenFingerprint() {
        setUpFingerprintContainer.hide()
        containerFingerprint.show()

        rxFingerprintManger.observeViewState().subscribe {
            when (it.state) {
                FingerprintViewState.State.START_LISTENING, FingerprintViewState.State.RESET_AFTER_ERROR ->
                    setIconAndText(R.drawable.ic_fingerprint, getString(R.string.title_fingerprint_start_listen))
                FingerprintViewState.State.AUTHENTICATION_HELP ->
                    setIconAndText(R.drawable.ic_fingerprint_error, it.meta?.message
                        ?: getString(R.string.title_default_help_text))
                FingerprintViewState.State.AUTHENTICATION_ERROR ->
                    setIconAndText(R.drawable.ic_fingerprint_error, it.meta?.message
                        ?: getString(R.string.title_common_error))
                FingerprintViewState.State.AUTHENTICATION_SUCCESS ->
                    setIconAndText(R.drawable.ic_fingerprint_success, getString(R.string.title_success))
            }
        }.addTo(compositeDisposable)

        val cryptoObject = FingerprintCrypton.prepareCryptoObjectSafe(RSA_KEY_ALIAS) {
            if (it is KeyPermanentlyInvalidatedException) {
                showAlert(getString(R.string.title_fingerprint_reset))
            } else {
                showAlert(getString(R.string.title_default_help_text))
            }
            bindAsSetupFingerprint()
        }

        cryptoObject?.let { nonNullCryptoObject ->
            rxFingerprintManger.startListening(nonNullCryptoObject)
                .subscribe({ cryptoObjectResult ->
                    val decryptedText = FingerprintCrypton.decrypt(cryptoObjectResult!!, preferences[KEY]!!)
                        ?: ""
                    showAlert(decryptedText)
                }, {
                    showAlert(it.toString())
                }).addTo(compositeDisposable)
        }

        buttonResetFingerprint.setOnClickListener {
            compositeDisposable.clear()
            preferences.remove(KEY)
            bindAsSetupFingerprint()
        }
    }

    private fun setIconAndText(icon: Int, text: String) {
        fingerprintStatusImageView.setImageResource(icon)
        statusTextView.text = text
    }

    private fun bindAsNotAvailable(currentFingerprintStatus: FingerprintStatus) {
        setUpFingerprintContainer.hide()
        containerFingerprint.show()
        fingerprintStatusImageView.setImageResource(R.drawable.ic_fingerprint_error)
        statusTextView.text = currentFingerprintStatus.toString()
        buttonResetFingerprint.hide()
        buttonFingerprintDialog.hide()
    }

    private fun showAlert(result: String) {
        AlertDialog.Builder(this)
            .setMessage(result)
            .setPositiveButton(R.string.btn_ok) { dialog, _ -> dialog?.dismiss() }
            .show()
    }
}