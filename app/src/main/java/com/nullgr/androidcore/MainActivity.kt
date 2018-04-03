package com.nullgr.androidcore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nullgr.androidcore.location.RxLocationManagerExampleActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonLocationExample.setOnClickListener {
            startActivity(Intent(this@MainActivity, RxLocationManagerExampleActivity::class.java))
        }
    }
}
