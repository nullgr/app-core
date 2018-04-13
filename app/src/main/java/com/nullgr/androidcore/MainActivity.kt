package com.nullgr.androidcore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nullgr.androidcore.fonts.FontsAndSpansExampleActivity
import com.nullgr.androidcore.intents.CommonIntentsExampleActivity
import com.nullgr.androidcore.location.RxLocationManagerExampleActivity
import com.nullgr.corelibrary.intents.launch
import kotlinx.android.synthetic.main.activity_main.*

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
    }
}
