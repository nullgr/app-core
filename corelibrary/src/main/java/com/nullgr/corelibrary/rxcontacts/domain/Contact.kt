package com.nullgr.corelibrary.rxcontacts.domain

import android.net.Uri

/**
 * Created by Grishko Nikita on 01.02.18.
 */
data class Contact(val id: Long,
                   val lookUpKey: String?,
                   val displayName: String?,
                   val isStarred: Boolean,
                   val photo: Uri?,
                   val thumbnail: Uri?,
                   val hasPhones: Boolean,
                   var emails: ArrayList<String>?,
                   var phones: ArrayList<String>?)