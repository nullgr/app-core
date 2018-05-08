package com.nullgr.corelibrary.rxcontacts.domain

/**
 * Created by Grishko Nikita on 01.02.18.
 */
//TODO add stared property
data class ContactPhone(override val id: Long,
                        override val displayName: String?,
                        val phoneNumber: String?,
                        val normalizedPhoneNumber: String?) : BaseContact