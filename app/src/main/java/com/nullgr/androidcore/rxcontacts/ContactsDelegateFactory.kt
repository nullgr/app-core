package com.nullgr.androidcore.rxcontacts

import com.nullgr.androidcore.rxcontacts.delegates.ContactDelegate
import com.nullgr.androidcore.rxcontacts.delegates.HeaderDelegate
import com.nullgr.androidcore.rxcontacts.items.ContactItem
import com.nullgr.androidcore.rxcontacts.items.HeaderItem
import com.nullgr.corelibrary.adapter.AdapterDelegate
import com.nullgr.corelibrary.adapter.AdapterDelegatesFactory
import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class ContactsDelegateFactory : AdapterDelegatesFactory {

    override fun createDelegate(clazz: Class<ListItem>): AdapterDelegate =
            when (clazz) {
                ContactItem::class.java -> ContactDelegate()
                HeaderItem::class.java -> HeaderDelegate()
                else -> throw IllegalArgumentException("No delegate defined for ${clazz.simpleName}")
            }
}