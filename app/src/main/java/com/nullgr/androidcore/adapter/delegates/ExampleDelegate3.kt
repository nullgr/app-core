package com.nullgr.androidcore.adapter.delegates

import android.support.v7.widget.RecyclerView
import com.nullgr.androidcore.R
import com.nullgr.androidcore.adapter.items.ExampleItem3
import com.nullgr.core.adapter.AdapterDelegate
import com.nullgr.core.adapter.items.ListItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_example_3.view.*

class ExampleDelegate3 : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_example_3
    override val itemType: Any = ExampleItem3::class

    override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder) {
        val item = items[position] as ExampleItem3

        with(holder.itemView) {
            Picasso.get().load(item.icon1).into(icon1View)
            textView.text = item.text
            Picasso.get().load(item.icon2).into(icon2View)
        }
    }
}