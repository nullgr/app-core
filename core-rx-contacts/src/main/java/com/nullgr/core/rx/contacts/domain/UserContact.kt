package com.nullgr.core.rx.contacts.domain

import android.net.Uri

/**
 * Created by Grishko Nikita on 01.02.18.
 */
data class UserContact(override val id: Long,
                       override val displayName: String?,
                       val lookUpKey: String?,
                       val isStarred: Boolean,
                       val photo: Uri?,
                       val thumbnail: Uri?,
                       val hasPhones: Boolean,
                       var phones: ArrayList<ContactPhone>?,
                       var emails: ArrayList<ContactEmail>?
) : BaseContact