package com.nullgr.androidcore.fonts

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nullgr.androidcore.R
import com.nullgr.corelibrary.fonts.absSize
import com.nullgr.corelibrary.fonts.applyColor
import com.nullgr.corelibrary.fonts.applyFont
import com.nullgr.corelibrary.fonts.applyRelativeSize
import com.nullgr.corelibrary.fonts.color
import com.nullgr.corelibrary.fonts.font
import com.nullgr.corelibrary.fonts.getTypeface
import com.nullgr.corelibrary.fonts.newSpanSet
import com.nullgr.corelibrary.fonts.relativeSize
import com.nullgr.corelibrary.fonts.setSpannableTitle
import com.nullgr.corelibrary.resources.spToPx
import kotlinx.android.synthetic.main.activity_fonts_and_spans_example.textWithColor
import kotlinx.android.synthetic.main.activity_fonts_and_spans_example.textWithComplicatedSpan
import kotlinx.android.synthetic.main.activity_fonts_and_spans_example.textWithFont
import kotlinx.android.synthetic.main.activity_fonts_and_spans_example.textWithRelativeSize

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
            val start2 = text.indexOf("r")
            val end2 = "Digital revolution".length
            val start3 = text.indexOf("T", ignoreCase = false)

            text = (newSpanSet()
                    add font(getTypeface("Roboto-Bold.ttf")) toText "Digital"
                    and font(getTypeface("Roboto-Italic.ttf")) from start2 to end2
                    and relativeSize(1.8f) toText "revolution"
                    and color(Color.GREEN) from start3
                    and absSize(28f.spToPx(context).toInt()) toText "we do."
                    ).applyTo(text.toString())

        }
    }
}