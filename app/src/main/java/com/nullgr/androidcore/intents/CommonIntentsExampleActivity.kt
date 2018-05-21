package com.nullgr.androidcore.intents

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nullgr.androidcore.R
import com.nullgr.corelibrary.intents.callIntent
import com.nullgr.corelibrary.intents.chromeTabsIntent
import com.nullgr.corelibrary.intents.emailIntent
import com.nullgr.corelibrary.intents.launch
import com.nullgr.corelibrary.intents.launchForResult
import com.nullgr.corelibrary.intents.navigationIntent
import com.nullgr.corelibrary.intents.selectContactIntent
import com.nullgr.corelibrary.intents.shareTextIntent
import com.nullgr.corelibrary.intents.webIntent
import com.nullgr.corelibrary.rxcontacts.RxContactsProvider
import com.nullgr.corelibrary.rxcontacts.domain.UserContact
import com.nullgr.corelibrary.ui.toast.showToast
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_common_intents_example.buttonCallTo
import kotlinx.android.synthetic.main.activity_common_intents_example.buttonMailTo
import kotlinx.android.synthetic.main.activity_common_intents_example.buttonRoteTo
import kotlinx.android.synthetic.main.activity_common_intents_example.buttonRxSelectContact
import kotlinx.android.synthetic.main.activity_common_intents_example.buttonShare
import kotlinx.android.synthetic.main.activity_common_intents_example.buttonWebChrome
import kotlinx.android.synthetic.main.activity_common_intents_example.buttonWebSimple

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
            emailIntent(getString(R.string.btn_null_gr_mail), body = getString(R.string.text_for_share))
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

                contactsDisposable = selectContactIntent()
                        .launchForResult(this)
                        .subscribe(
                                {
                                    it.intent?.let { intent ->
                                        RxContactsProvider.with(this)
                                                .fromUri(UserContact::class.java, intent.data)
                                                .subscribe(
                                                        {
                                                            ("Contact (${it.firstOrNull()?.displayName}) " +
                                                                    "found, from uri ${intent.data}").showToast(this)
                                                        },
                                                        {

                                                            "Error while fetching contacts from uri ($it)".showToast(this)
                                                        })
                                    }
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