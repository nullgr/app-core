package com.nullgr.androidcore.adapter.delegates

import android.support.v7.widget.RecyclerView
import com.nullgr.androidcore.R
import com.nullgr.androidcore.adapter.items.ExampleItem1
import com.nullgr.core.adapter.AdapterDelegate
import com.nullgr.core.adapter.items.ListItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_example_1.view.*

class ExampleDelegate1 : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_example_1
    override val itemType: Any = ExampleItem1::class

    override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder) {
        val item = items[position] as ExampleItem1

        with(holder.itemView) {
            Picasso.get().load(item.icon).into(iconView)
            textView.text = item.text
        }
    }
}