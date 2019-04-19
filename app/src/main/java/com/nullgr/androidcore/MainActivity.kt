package com.nullgr.androidcore

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nullgr.androidcore.adapter.AdapterExampleActivity
import com.nullgr.androidcore.adapter.AdapterExampleWithPayloadsActivity
import com.nullgr.androidcore.date.DateUtilsExampleActivity
import com.nullgr.androidcore.fingerprint.FingerprintExampleActivity
import com.nullgr.androidcore.fonts.FontsAndSpansExampleActivity
import com.nullgr.androidcore.intents.CommonIntentsExampleActivity
import com.nullgr.androidcore.interactor.presentation.InteractorExampleActivity
import com.nullgr.androidcore.keyboardanimator.KeyboardAnimatorExampleActivity
import com.nullgr.androidcore.location.RxLocationManagerExampleActivity
import com.nullgr.androidcore.resources.ResourcesExampleActivity
import com.nullgr.androidcore.rxcontacts.RxContactsExampleActivity
import com.nullgr.core.common.withVersion
import com.nullgr.core.intents.launch
import com.nullgr.core.ui.toast.showLongToast
import kotlinx.android.synthetic.main.activity_main.buttonAdapterExample
import kotlinx.android.synthetic.main.activity_main.buttonAdapterWithPayloadsExample
import kotlinx.android.synthetic.main.activity_main.buttonCommonIntentsExample
import kotlinx.android.synthetic.main.activity_main.buttonDateUtilsExample
import kotlinx.android.synthetic.main.activity_main.buttonFingerprintExample
import kotlinx.android.synthetic.main.activity_main.buttonFontsAndSpans
import kotlinx.android.synthetic.main.activity_main.buttonInteractorsExample
import kotlinx.android.synthetic.main.activity_main.buttonLocationExample
import kotlinx.android.synthetic.main.activity_main.buttonResourcesExample
import kotlinx.android.synthetic.main.activity_main.buttonRxContactsProviderExample
import kotlinx.android.synthetic.main.activity_main.buttonKeyboardAnimatorExample

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonLocationExample.setOnClickListener {
            Intent(this@MainActivity, RxLocationManagerExampleActivity::class.java)
                .launch(this)
        }

        buttonCommonIntentsExample.setOnClickListener {
            Intent(this, CommonIntentsExampleActivity::class.java)
                .launch(this)
        }

        buttonFontsAndSpans.setOnClickListener {
            Intent(this, FontsAndSpansExampleActivity::class.java)
                .launch(this)
        }

        buttonDateUtilsExample.setOnClickListener {
            Intent(this, DateUtilsExampleActivity::class.java).launch(this)
        }

        buttonResourcesExample.setOnClickListener {
            Intent(this, ResourcesExampleActivity::class.java).launch(this)
        }

        buttonAdapterExample.setOnClickListener {
            Intent(this, AdapterExampleActivity::class.java).launch(this)
        }

        buttonAdapterWithPayloadsExample.setOnClickListener {
            Intent(this, AdapterExampleWithPayloadsActivity::class.java).launch(this)
        }

        buttonRxContactsProviderExample.setOnClickListener {
            Intent(this, RxContactsExampleActivity::class.java).launch(this)
        }

        buttonInteractorsExample.setOnClickListener {
            Intent(this, InteractorExampleActivity::class.java).launch(this)
        }

        buttonFingerprintExample.setOnClickListener {
            withVersion(Build.VERSION_CODES.M) {
                higherOrEqual { Intent(this@MainActivity, FingerprintExampleActivity::class.java)
                    .launch(this@MainActivity) }
                lower { getString(R.string.alert_lower_api, versionCode).showLongToast(this@MainActivity) }
            }
        }

        buttonKeyboardAnimatorExample.setOnClickListener {
            withVersion(Build.VERSION_CODES.LOLLIPOP) {
                higherOrEqual {
                    Intent(this@MainActivity, KeyboardAnimatorExampleActivity::class.java).launch(this@MainActivity)
                }
                lower {
                    getString(R.string.alert_lower_api, versionCode).showLongToast(this@MainActivity)
                }
            }
        }
    }
}
