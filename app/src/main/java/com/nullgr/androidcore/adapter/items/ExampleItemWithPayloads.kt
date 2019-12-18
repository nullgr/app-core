package com.nullgr.androidcore.adapter.items

import androidx.annotation.ColorInt
import com.nullgr.core.adapter.items.ListItem

/**
 * @author chernyshov.
 */
data class ExampleItemWithPayloads(val title: String, val subTitle: String, @ColorInt val color: Int) : ListItem {

    override fun getChangePayload(other: ListItem): Any {
        if (this::class == other::class) {
            other as ExampleItemWithPayloads
            return mutableSetOf<Payload>().apply {
                if (title != other.title) add(Payload.TITLE_CHANGED)
                if (subTitle != other.subTitle) add(Payload.SUB_TITLE_CHANGED)
                if (color != other.color) add(Payload.COLOR_CHANGED)
            }
        }
        return super.getChangePayload(other)
    }

    enum class Payload {
        TITLE_CHANGED, SUB_TITLE_CHANGED, COLOR_CHANGED
    }
}