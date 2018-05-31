package com.nullgr.androidcore.interactor.presentation.adapter.delegates

import android.support.v7.widget.RecyclerView
import com.nullgr.androidcore.R
import com.nullgr.androidcore.interactor.presentation.adapter.items.InteractorItem
import com.nullgr.corelibrary.adapter.AdapterDelegate
import com.nullgr.corelibrary.adapter.items.ListItem
import com.nullgr.corelibrary.rx.RxBus
import kotlinx.android.synthetic.main.item_interactor.view.*

class InteractorDelegate(private val bus: RxBus) : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_interactor
    override val itemType: Any = InteractorItem::class

    override fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder) {
        val item = items[position] as InteractorItem

        with(vh.itemView) {
            interactorTitleView.text = item.title
            executeButtonView.setOnClickListener {
                bus.post(event = item)
            }
        }
    }
}