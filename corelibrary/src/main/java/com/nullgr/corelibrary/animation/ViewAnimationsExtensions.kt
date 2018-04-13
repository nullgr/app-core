package com.nullgr.corelibrary.animation

import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import com.nullgr.corelibrary.common.forVersion
import com.nullgr.corelibrary.ui.disappear
import com.nullgr.corelibrary.ui.show


/**
 * Created by Grishko Nikita on 01.02.18.
 */
infix fun <T : View> T.crossFadeTo(other: T) {
    this.animate().alpha(0f)?.doOnEnd {
        this.disappear()
        this.alpha = 1f
        other.alpha = 0f
        other.show()
        other.animate().alpha(1f)
    }
}

infix fun <T : View> T.revealTo(other: T) {
    forVersion(Build.VERSION_CODES.LOLLIPOP)
            .doIfHigher {
                val width = this.width
                val height = this.height
                val maxRadius = Math.sqrt((width * width / 4 + height * height / 4).toDouble()).toFloat()
                val reveal = ViewAnimationUtils.createCircularReveal(other, width / 2, height / 2, 0f, maxRadius)
                other.show()
                this.disappear()
                reveal.start()
            }
            .doIfLower {
                this.disappear()
                other.show()
            }
}