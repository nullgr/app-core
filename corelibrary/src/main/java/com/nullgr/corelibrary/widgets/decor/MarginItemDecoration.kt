package kz.altel.mobile.ss.core.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author chernyshov.
 */
class MarginItemDecoration(
        context: Context,
        @DimenRes
        private val marginStartDimen: Int,
        @DimenRes
        private val marginEndDimen: Int,
        @DimenRes
        private var marginBetweenDimen: Int
) : RecyclerView.ItemDecoration() {

    private var marginStart: Int
    private var marginEnd: Int
    private var marginBetween: Int

    init {
        marginStart = getPixelSize(context.resources, marginStartDimen)
        marginEnd = getPixelSize(context.resources, marginEndDimen)
        marginBetween = getPixelSize(context.resources, marginBetweenDimen)
        marginBetween = if (marginBetween != 0) marginBetween / 2 else 0
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        val position = parent?.getChildAdapterPosition(view)
        val last = state?.itemCount?.minus(1)
        when (position) {
            0 -> {
                outRect?.left = marginStart
                outRect?.right = marginBetween
            }
            last -> {
                outRect?.left = marginBetween
                outRect?.right = marginEnd
            }
            else -> {
                outRect?.left = marginBetween
                outRect?.right = marginBetween
            }
        }
    }

    private fun getPixelSize(resources: Resources, @DimenRes margin: Int): Int {
        return if (margin == 0) 0 else resources.getDimensionPixelSize(margin)
    }
}