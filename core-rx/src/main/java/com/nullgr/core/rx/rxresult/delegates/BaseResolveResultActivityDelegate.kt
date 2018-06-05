package com.nullgr.core.rx.rxresult.delegates

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import com.nullgr.core.rx.RxBus
import com.nullgr.core.rx.SingletonRxBusProvider
import com.nullgr.core.rx.rxresult.RxActivityResult

/**
 * @author Grishko Nikita
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
        SingletonRxBusProvider.BUS.post(RxBus.Keys.SINGLE, RxActivityResult(data, resultCode))
        activity.finish()
        activity.overridePendingTransition(0, 0)
    }
}