package com.nullgr.corelibrary.rx.rxresult.delegates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.nullgr.corelibrary.rx.SingletonRxBusProvider
import com.nullgr.corelibrary.rx.rxresult.RxActivityResult

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal abstract class BaseResolveResultActivityDelegate(val activity: Activity) {

    companion object {
        const val REQUEST_CODE = 1200

        fun newInstance(clazz: Class<*>, activity: Activity): BaseResolveResultActivityDelegate {
            return when (clazz) {
                LocationSettingsResultActivityDelegate::class.java -> LocationSettingsResultActivityDelegate(activity)
                SimpleResultActivityDelegate::class.java -> SimpleResultActivityDelegate(activity)
                else -> throw IllegalArgumentException("No delegate for class $clazz")
            }
        }
    }

    abstract fun onCreate(savedInstanceState: Bundle?)

    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    protected fun sendResult(resultCode: Int, data: Intent?) {
        SingletonRxBusProvider.BUS.eventsConsumer.accept(RxActivityResult(data, resultCode))
        activity.finish()
        activity.overridePendingTransition(0, 0)
    }
}