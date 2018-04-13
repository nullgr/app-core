package com.nullgr.androidcore.intents

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nullgr.androidcore.R
import com.nullgr.corelibrary.intents.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_common_intents_example.*

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class CommonIntentsExampleActivity : AppCompatActivity() {

    private lateinit var contactsDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_intents_example)

        val errorText = getString(R.string.error_activity_not_found)

        buttonWebSimple.setOnClickListener {
            webIntent(getString(R.string.btn_null_gr_web))
                    .launch(this, errorText)
        }

        buttonWebChrome.setOnClickListener {
            chromeTabsIntent(ContextCompat.getColor(this, R.color.colorPrimary))
                    .launch(this, getString(R.string.btn_null_gr_web))
        }

        buttonCallTo.setOnClickListener {
            callIntent(getString(R.string.btn_null_gr_phone))
                    .launch(this, errorText)
        }

        buttonMailTo.setOnClickListener {
            emailIntent(getString(R.string.btn_null_gr_phone), body = getString(R.string.text_for_share))
                    .launch(this, errorText)
        }

        buttonRoteTo.setOnClickListener {
            navigationIntent(50.434195, 30.686477)
                    .launch(this, errorText)
        }

        buttonShare.setOnClickListener {
            shareTextIntent(getString(R.string.text_for_share))
                    .launch(this, errorText)
        }

        // startActivityForResult in RX way
        // declare this activity in your manifest file to use
        // CommonIntentsExtensions#launchForResult (need only for rx version of this method)
        // <activity android : name ="com.nullgr.corelibrary.rx.rxresult.RxResolveResultActivity" />

        buttonRxSelectContact.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {

                contactsDisposable = selectContactPhoneIntent()
                        .launchForResult(this)
                        .subscribe(
                                {
                                    Toast.makeText(this,
                                            "resultCode=${it.resultCode}, data=${it.intent}", Toast.LENGTH_SHORT).show()
                                },
                                { Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show() })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::contactsDisposable.isInitialized) {
            contactsDisposable.dispose()
        }
    }
}