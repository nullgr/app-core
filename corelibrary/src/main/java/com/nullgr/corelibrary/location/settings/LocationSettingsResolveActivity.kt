package com.nullgr.corelibrary.location.settings

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewStub
import com.nullgr.corelibrary.rx.SingletonRxBusProvider

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class LocationSettingsResolveActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
        const val EXTRA_INTENT_SENDER = "extra_intent_sender"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ViewStub(this))
        if (intent.hasExtra(EXTRA_INTENT_SENDER)) {
            val intentSender = intent.getParcelableExtra(EXTRA_INTENT_SENDER) as IntentSender
            startIntentSenderForResult(intentSender, REQUEST_CHECK_SETTINGS, null as Intent?, 0, 0, 0)
        } else {
            sendResult(Activity.RESULT_CANCELED)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            sendResult(resultCode)
        }
    }

    private fun sendResult(resultCode: Int) {
        SingletonRxBusProvider.BUS.eventsConsumer.accept(LocationSettingsChangeEvent(resultCode))
        finish()
        overridePendingTransition(0, 0)
    }
}