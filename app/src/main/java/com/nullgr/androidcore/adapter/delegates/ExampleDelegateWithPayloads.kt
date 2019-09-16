package com.nullgr.androidcore.adapter.delegates

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nullgr.androidcore.R
import com.nullgr.androidcore.adapter.Event
import com.nullgr.androidcore.adapter.items.ExampleItemWithPayloads
import com.nullgr.core.adapter.AdapterDelegate
import com.nullgr.core.adapter.items.ListItem
import com.nullgr.core.adapter.withAdapterPosition
import com.nullgr.core.rx.RxBus
import kotlinx.android.synthetic.main.item_example_with_payloads.view.*

class ExampleDelegateWithPayloads(private val bus: RxBus) : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_example_with_payloads
    override val itemType: Any = ExampleItemWithPayloads::class

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent).apply {
            val onClickListener = View.OnClickListener { view ->
                this.withAdapterPosition<ExampleItemWithPayloads> {item, _ ->
                    when (view.id) {
                        R.id.colorView -> bus.post(Event.Click("child colorView"))
                        else -> bus.post(Event.Click(item))
                    }
                }
            }
            this.itemView.colorView.setOnClickListener(onClickListener)
            this.itemView.setOnClickListener(onClickListener)
        }
    }

    override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder) {
        val item = items[position] as ExampleItemWithPayloads

        with(holder.itemView) {
            titleView.text = item.title
            subTitleView.text = item.subTitle
            colorView.setBackgroundColor(item.color)
        }

        bus.post(Event.Payload("onBindViewHolder"))
    }

    override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder, payload: Any) {
        val item = items[position] as ExampleItemWithPayloads
        when (payload) {
            ExampleItemWithPayloads.Payload.TITLE_CHANGED -> {
                holder.itemView.titleView.text = item.title
            }
            ExampleItemWithPayloads.Payload.SUB_TITLE_CHANGED -> {
                holder.itemView.subTitleView.text = item.subTitle
            }
            ExampleItemWithPayloads.Payload.COLOR_CHANGED -> {
                holder.itemView.colorView.setBackgroundColor(item.color)
            }
        }
        bus.post(Event.Payload(payload))
    }
}