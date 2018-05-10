package com.nullgr.androidcore.rxcontacts

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.nullgr.androidcore.R
import com.nullgr.androidcore.adapter.BaseAdapterExampleActivity
import com.nullgr.corelibrary.widgets.decor.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_adapter_example.itemsView

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class RxContactsExampleActivity : BaseAdapterExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_contacts_example_activity)

        itemsView.layoutManager = LinearLayoutManager(this)
        itemsView.adapter = adapter
        itemsView.addItemDecoration(DividerItemDecoration(this, R.drawable.divider_adapter_item))
    }
}