package com.nullgr.androidcore.fonts

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import com.nullgr.androidcore.R
import com.nullgr.corelibrary.fonts.*
import kotlinx.android.synthetic.main.activity_fonts_and_spans_example.*

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class FontsAndSpansExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fonts_and_spans_example)

        supportActionBar?.setSpannableTitle(this, "Awesome title", "Roboto-Bold.ttf")

        with(textWithColor) {
            text = text.toString().applyColor(Color.RED, 0, 7)
        }

        with(textWithFont) {
            text = text.toString().applyFont(this@FontsAndSpansExampleActivity, "Roboto-Bold.ttf")
        }

        with(textWithRelativeSize) {
            text = text.toString().applyRelativeSize(1.5f, 0, 7)
        }

        with(textWithComplicatedSpan) {

            val end1 = "Digital".length

            val start2 = text.indexOf("r")
            val end2 = "Digital revolution".length

            val start3 = text.indexOf("T", ignoreCase = false)

            text = (newSpanSet()
                    add TypefaceSpan(getTypeface("Roboto-Bold.ttf")) to end1
                    and TypefaceSpan(getTypeface("Roboto-Italic.ttf")) from start2 to end2
                    and RelativeSizeSpan(1.8f) from start2 to end2
                    and ForegroundColorSpan(Color.GREEN) from start3
                    ).applyTo(text.toString())

        }
    }
}