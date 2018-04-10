package com.nullgr.corelibrary.rx.rxresult.delegates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.nullgr.corelibrary.rx.rxresult.RxResolveResultActivity

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal class SimpleResultActivityDelegate(activity: Activity) : BaseResolveResultActivityDelegate(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (activity.intent.hasExtra(RxResolveResultActivity.EXTRA_KEY)) {
            val intent = activity.intent.extras[RxResolveResultActivity.EXTRA_KEY] as Intent
            activity.startActivityForResult(intent, REQUEST_CODE)
        } else {
            sendResult(Activity.RESULT_CANCELED, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            sendResult(resultCode, data)
        }
    }
}