package com.nullgr.corelibrary.intents.rxresult

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nullgr.corelibrary.rx.SingletonRxBusProvider

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class RxResolveResultActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_KEY = "extra_intent"
        private const val REQUEST_CODE = 1200

        fun newInstance(context: Context, innerIntent: Intent): Intent {
            return Intent(context, RxResolveResultActivity::class.java)
                    .apply {
                        putExtra(EXTRA_KEY, innerIntent)
                        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(EXTRA_KEY)) {
            val intent = intent.extras[EXTRA_KEY] as Intent
            startActivityForResult(intent, REQUEST_CODE)
        } else {
            sendResult(Activity.RESULT_CANCELED, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            sendResult(resultCode, data)
        }
    }

    private fun sendResult(resultCode: Int, data: Intent?) {
        SingletonRxBusProvider.BUS.eventsConsumer.accept(RxActivityResult(data, resultCode))
        finish()
        overridePendingTransition(0, 0)
    }
}