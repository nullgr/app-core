package com.nullgr.androidcore.rxcontacts.delegates

import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.nullgr.androidcore.R
import com.nullgr.androidcore.rxcontacts.items.HeaderItem
import com.nullgr.core.adapter.AdapterDelegate
import com.nullgr.core.adapter.items.ListItem

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class HeaderDelegate : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_header
    override val itemType: Any = HeaderItem::class

    override fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder) {
        val item = items[position] as HeaderItem
        (vh.itemView as TextView).text = item.title
    }
}