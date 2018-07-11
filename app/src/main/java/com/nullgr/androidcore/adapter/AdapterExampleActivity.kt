package com.nullgr.androidcore.adapter

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.nullgr.androidcore.R
import com.nullgr.androidcore.adapter.items.ExampleItem1
import com.nullgr.androidcore.adapter.items.ExampleItem2
import com.nullgr.androidcore.adapter.items.ExampleItem3
import com.nullgr.core.adapter.items.ListItem
import com.nullgr.core.collections.replace
import com.nullgr.core.ui.decor.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_adapter_example.addItemButton
import kotlinx.android.synthetic.main.activity_adapter_example.itemsView
import kotlinx.android.synthetic.main.activity_adapter_example.removeItemButton
import java.util.*

/**
 * @author chernyshov.
 */
class AdapterExampleActivity : BaseAdapterExampleActivity() {

    private val random = Random()
    private val items = arrayListOf<ListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter_example)
        itemsView.layoutManager = LinearLayoutManager(this)
        itemsView.adapter = adapter
        itemsView.addItemDecoration(DividerItemDecoration(this, R.drawable.divider_adapter_item))

        items.replace(prepareExampleItems())
        adapter.updateData(newItems = items, enableDiffUtils = true, detectMoves = true)

        addItemButton.setOnClickListener {
            items.add(2, ExampleItem2(Data.URLS[random.nextInt(Data.URLS.size)],
                "Added ExampleItem ${random.nextInt(100)}",
                "SomeText2"))
            adapter.updateData(newItems = items, enableDiffUtils = true, detectMoves = true)
        }

        removeItemButton.setOnClickListener {
            if (items.size > 2)
                items.removeAt(2)
            adapter.updateData(newItems = items, enableDiffUtils = true, detectMoves = true)
        }
    }

    override fun provideDelegatesFactory() = ExampleDelegatesFactory(bus)

    private fun prepareExampleItems(): List<ListItem> =
        arrayListOf(
            ExampleItem1(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem1-1"),
            ExampleItem1(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem1-2"),
            ExampleItem1(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem1-3"),
            ExampleItem1(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem1-4"),
            ExampleItem1(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem1-5"),
            ExampleItem2(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem2-1", "ExampleItem"),
            ExampleItem2(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem2-2", "ExampleItem"),
            ExampleItem2(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem2-3", "ExampleItem"),
            ExampleItem2(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem2-4", "ExampleItem"),
            ExampleItem2(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem2-5", "ExampleItem"),
            ExampleItem3(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem3-1", Data.URLS[random.nextInt(Data.URLS.size)]),
            ExampleItem3(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem3-2", Data.URLS[random.nextInt(Data.URLS.size)]),
            ExampleItem3(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem3-3", Data.URLS[random.nextInt(Data.URLS.size)]),
            ExampleItem3(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem3-4", Data.URLS[random.nextInt(Data.URLS.size)]),
            ExampleItem3(Data.URLS[random.nextInt(Data.URLS.size)], "ExampleItem3-5", Data.URLS[random.nextInt(Data.URLS.size)])
        )

    internal object Data {
        val BASE = "http://i.imgur.com/"
        val EXT = ".jpg"
        val URLS = arrayOf(
            BASE + "CqmBjo5" + EXT, BASE + "zkaAooq" + EXT, BASE + "0gqnEaY" + EXT,
            BASE + "9gbQ7YR" + EXT, BASE + "aFhEEby" + EXT, BASE + "0E2tgV7" + EXT,
            BASE + "P5JLfjk" + EXT, BASE + "nz67a4F" + EXT, BASE + "dFH34N5" + EXT,
            BASE + "FI49ftb" + EXT, BASE + "DvpvklR" + EXT, BASE + "DNKnbG8" + EXT,
            BASE + "yAdbrLp" + EXT, BASE + "55w5Km7" + EXT, BASE + "NIwNTMR" + EXT,
            BASE + "DAl0KB8" + EXT, BASE + "xZLIYFV" + EXT, BASE + "HvTyeh3" + EXT,
            BASE + "Ig9oHCM" + EXT, BASE + "7GUv9qa" + EXT, BASE + "i5vXmXp" + EXT,
            BASE + "glyvuXg" + EXT, BASE + "u6JF6JZ" + EXT, BASE + "ExwR7ap" + EXT,
            BASE + "Q54zMKT" + EXT, BASE + "9t6hLbm" + EXT, BASE + "F8n3Ic6" + EXT,
            BASE + "P5ZRSvT" + EXT, BASE + "jbemFzr" + EXT, BASE + "8B7haIK" + EXT,
            BASE + "aSeTYQr" + EXT, BASE + "OKvWoTh" + EXT, BASE + "zD3gT4Z" + EXT,
            BASE + "z77CaIt" + EXT)
    }
}