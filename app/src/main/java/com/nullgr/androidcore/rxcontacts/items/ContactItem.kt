package com.nullgr.androidcore.rxcontacts.items

import android.net.Uri
import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * Created by Grishko Nikita on 01.02.18.
 */
data class ContactItem(val id: Int,
                       val displayName: String,
                       val isFavorite: Boolean,
                       val photo: Uri,
                       var phones: ArrayList<String>?,
                       var emails: ArrayList<String>?) : ListItem {

    override fun getUniqueProperty() = id
}