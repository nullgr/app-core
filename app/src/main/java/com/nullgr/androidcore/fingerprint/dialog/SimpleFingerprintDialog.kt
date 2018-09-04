package com.nullgr.androidcore.fingerprint.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.nullgr.androidcore.R
import com.nullgr.core.security.fingerprint.FingerprintAuthenticationManager
import com.nullgr.core.security.fingerprint.FingerprintResultListener
import com.nullgr.core.security.fingerprint.FingerprintView
import com.nullgr.core.ui.extensions.getDisplaySize
import kotlinx.android.synthetic.main.dialog_simple_fingerprint.fingerprintStatusImageView
import kotlinx.android.synthetic.main.dialog_simple_fingerprint.statusTextView

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class SimpleFingerprintDialog : DialogFragment(), FingerprintView {

    companion object {
        fun getInstance() = SimpleFingerprintDialog()
    }

    private lateinit var fingerprintAuthenticationManager: FingerprintAuthenticationManager

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
        activity?.let {
            dialog?.window?.setLayout((getDisplaySize(it).first * 0.8).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_simple_fingerprint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            fingerprintAuthenticationManager = FingerprintAuthenticationManager
                .from(it)
                .attachView(this)
                .withResultListener(object : FingerprintResultListener {
                    override fun onSuccess(cryptoObject: FingerprintManagerCompat.CryptoObject?) {
                        dismissAllowingStateLoss()
                    }
                })
                .build()
            fingerprintAuthenticationManager.startListening()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::fingerprintAuthenticationManager.isInitialized)
            fingerprintAuthenticationManager.stopListening()
    }

    override fun onShowFingerprintListening() {
        fingerprintStatusImageView.setImageResource(R.drawable.ic_fingerprint_dark)
        statusTextView.text = getString(R.string.title_fingerprint_start_listen)
    }

    override fun onShowAuthenticationSuccess() {
        fingerprintStatusImageView.setImageResource(R.drawable.ic_fingerprint_success)
        statusTextView.text = getString(R.string.title_success)
    }

    override fun onShowAuthenticationError(errorMessageId: Int?, errorMessage: String?) {
        fingerprintStatusImageView.setImageResource(R.drawable.ic_fingerprint_error)
        statusTextView.text = errorMessage ?: getString(R.string.title_common_error)
    }

    override fun onShowAuthenticationHelp(helpMessageId: Int, helpMessage: String) {
        fingerprintStatusImageView.setImageResource(R.drawable.ic_fingerprint_error)
        statusTextView.text = helpMessage
    }

    override fun onResetFingerprintUiStateAfterError() {
        onShowFingerprintListening()
    }
}