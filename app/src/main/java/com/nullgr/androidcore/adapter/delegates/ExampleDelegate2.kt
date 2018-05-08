package com.nullgr.androidcore.adapter.delegates

import android.support.v7.widget.RecyclerView
import com.nullgr.androidcore.R
import com.nullgr.androidcore.adapter.items.ExampleItem2
import com.nullgr.corelibrary.adapter.AdapterDelegate
import com.nullgr.corelibrary.adapter.items.ListItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_example_2.view.*

class ExampleDelegate2 : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_example_2
    override val itemType: Any = ExampleItem2::class

    override fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder) {
        val item = items[position] as ExampleItem2

        with(vh.itemView) {
            Picasso.get().load(item.icon).into(iconView)
            text1View.text = item.text1
            text2View.text = item.text2
        }
    }
}