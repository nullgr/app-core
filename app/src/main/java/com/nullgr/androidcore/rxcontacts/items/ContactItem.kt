package com.nullgr.androidcore.rxcontacts.items

import android.net.Uri
import com.nullgr.core.adapter.items.ListItem

/**
 * Created by Grishko Nikita on 01.02.18.
 */
data class ContactItem(val id: Long,
                       val displayName: String?,
                       val isFavorite: Boolean,
                       val photo: Uri?,
                       var phones: List<String>?,
                       var emails: List<String>?) : ListItem {

    override fun getUniqueProperty() = id
}