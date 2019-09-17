package com.nullgr.androidcore.fonts

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nullgr.androidcore.R
import com.nullgr.core.font.absSize
import com.nullgr.core.font.applyForegroundColor
import com.nullgr.core.font.applyFont
import com.nullgr.core.font.applyRelativeSize
import com.nullgr.core.font.applySpanSet
import com.nullgr.core.font.backgroundColor
import com.nullgr.core.font.font
import com.nullgr.core.font.foregroundColor
import com.nullgr.core.font.getTypeface
import com.nullgr.core.font.relativeSize
import com.nullgr.core.font.setSpannableTitle
import com.nullgr.core.font.typeface
import com.nullgr.core.font.withSpan
import com.nullgr.core.ui.extensions.spToPx
import kotlinx.android.synthetic.main.activity_fonts_and_spans_example.textWithColor
import kotlinx.android.synthetic.main.activity_fonts_and_spans_example.textWithComplicatedSpan
import kotlinx.android.synthetic.main.activity_fonts_and_spans_example.textWithComplicatedSpan2
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
            text = text.toString().applyForegroundColor(Color.RED, 0, 7)
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

            text = (text.toString().applySpanSet()
                    add font(getTypeface("Roboto-Bold.ttf")) toText "Digital"
                    and font(getTypeface("Roboto-Italic.ttf")) from start2 to end2
                    and relativeSize(1.8f) toText "revolution"
                    and foregroundColor(Color.GREEN) from start3
                    and absSize(28f.spToPx(context).toInt()) toText "we do.").build()
        }

        with(textWithComplicatedSpan2) {

            val start2 = text.indexOf("r")
            val end2 = "Digital revolution".length
            val start3 = text.indexOf("T", ignoreCase = false)

            text = text.withSpan {

                typeface {
                    typeface = getTypeface("Roboto-Bold.ttf")
                    toText = "Digital"
                }

                typeface {
                    typeface = getTypeface("Roboto-Bold.ttf")
                    from = start2
                    to = end2
                }

                relativeSize {
                    size = 1.8f
                    toText = "revolution"
                }

                backgroundColor {
                    color = Color.LTGRAY
                    toText = "Digi"
                }

                foregroundColor {
                    color = Color.GREEN
                    from = start3
                }
            }
        }
    }
}