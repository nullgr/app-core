package com.nullgr.corelibrary.rx.rxresult

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import com.nullgr.corelibrary.rx.rxresult.delegates.BaseResolveResultActivityDelegate

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal class RxResolveResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_KEY = "extra_intent"

        fun newInstance(context: Context, argument: Parcelable): Intent {
            return Intent(context, RxResolveResultActivity::class.java)
                    .apply {
                        putExtra(EXTRA_KEY, argument)
                        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    }
        }
    }

    private val delegate: BaseResolveResultActivityDelegate by lazy {
        BaseResolveResultActivityDelegate.newInstance(intent.extras[EXTRA_KEY], this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        delegate.onActivityResult(requestCode, resultCode, data)
    }
}