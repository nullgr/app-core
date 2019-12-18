package com.nullgr.androidcore.interactor.presentation.adapter.delegates

import androidx.recyclerview.widget.RecyclerView
import com.nullgr.androidcore.R
import com.nullgr.androidcore.interactor.presentation.adapter.items.InteractorItem
import com.nullgr.core.adapter.AdapterDelegate
import com.nullgr.core.adapter.items.ListItem
import com.nullgr.core.rx.RxBus
import kotlinx.android.synthetic.main.item_interactor.view.*

class InteractorDelegate(private val bus: RxBus) : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_interactor
    override val itemType: Any = InteractorItem::class

    override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder) {
        val item = items[position] as InteractorItem

        with(holder.itemView) {
            interactorTitleView.text = item.title
            executeButtonView.setOnClickListener {
                bus.post(event = item)
            }
        }
    }
}