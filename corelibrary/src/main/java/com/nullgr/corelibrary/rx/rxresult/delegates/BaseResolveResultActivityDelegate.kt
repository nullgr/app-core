package com.nullgr.corelibrary.rx.rxresult.delegates

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import com.nullgr.corelibrary.rx.RxBus
import com.nullgr.corelibrary.rx.SingletonRxBusProvider
import com.nullgr.corelibrary.rx.rxresult.RxActivityResult

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal abstract class BaseResolveResultActivityDelegate(val activity: Activity) {

    companion object {
        const val REQUEST_CODE = 1200

        fun newInstance(argument: Any, activity: Activity): BaseResolveResultActivityDelegate {
            return when (argument::class.java) {
                IntentSender::class.java -> IntentSenderActivityDelegate(activity)
                Intent::class.java -> IntentActivityDelegate(activity)
                else -> throw IllegalArgumentException("No delegate for class ${argument::class.java}")
            }
        }
    }

    abstract fun onCreate(savedInstanceState: Bundle?)

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            sendResult(resultCode, data)
        }
    }

    protected fun sendResult(resultCode: Int, data: Intent?) {
        SingletonRxBusProvider.BUS.post(RxBus.KEYS.SINGLE, RxActivityResult(data, resultCode))
        activity.finish()
        activity.overridePendingTransition(0, 0)
    }
}