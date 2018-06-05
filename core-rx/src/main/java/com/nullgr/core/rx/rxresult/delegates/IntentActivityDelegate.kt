package com.nullgr.core.rx.rxresult.delegates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.nullgr.core.rx.rxresult.RxResolveResultActivity

/**
 * @author Grishko Nikita
 */
internal class IntentActivityDelegate(activity: Activity) : BaseResolveResultActivityDelegate(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (activity.intent.hasExtra(RxResolveResultActivity.EXTRA_KEY)) {
            val intent = activity.intent.extras[RxResolveResultActivity.EXTRA_KEY] as Intent
            activity.startActivityForResult(intent, REQUEST_CODE)
        } else {
            sendResult(Activity.RESULT_CANCELED, null)
        }
    }
}