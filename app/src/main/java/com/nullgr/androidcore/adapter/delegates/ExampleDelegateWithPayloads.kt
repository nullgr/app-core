package com.nullgr.androidcore.adapter.delegates

import android.support.v7.widget.RecyclerView
import com.nullgr.androidcore.R
import com.nullgr.androidcore.adapter.Event
import com.nullgr.androidcore.adapter.items.ExampleItemWithPayloads
import com.nullgr.corelibrary.adapter.AdapterDelegate
import com.nullgr.corelibrary.adapter.items.ListItem
import com.nullgr.corelibrary.rx.RxBus
import kotlinx.android.synthetic.main.item_example_with_payloads.view.colorView
import kotlinx.android.synthetic.main.item_example_with_payloads.view.subTitleView
import kotlinx.android.synthetic.main.item_example_with_payloads.view.titleView

class ExampleDelegateWithPayloads(private val bus: RxBus) : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_example_with_payloads
    override val itemType: Any = ExampleItemWithPayloads::class

    override fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder) {
        val item = items[position] as ExampleItemWithPayloads

        with(vh.itemView) {
            titleView.text = item.title
            subTitleView.text = item.subTitle
            colorView.setBackgroundColor(item.color)
            setOnClickListener {
                bus.post(event = Event.Click(item))
            }
        }

        bus.post(event = Event.Payload("onBindViewHolder"))
    }

    override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        if (payloads.isNotEmpty()) {
            val item = items[position] as ExampleItemWithPayloads
            payloads.forEach { payload ->
                if (payload is Collection<*>) {
                    payload.forEach {
                        when (it) {
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
                    }
                    holder.itemView.setOnClickListener {
                        bus.post(event = Event.Click(item))
                    }
                    bus.post(event = Event.Payload(payload))
                }
            }
        }
    }
}