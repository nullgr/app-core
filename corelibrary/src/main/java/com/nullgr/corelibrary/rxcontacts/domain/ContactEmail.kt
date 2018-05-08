package com.nullgr.corelibrary.rxcontacts.domain

/**
 * Created by Grishko Nikita on 01.02.18.
 */
//TODO add stared property
//TODO check why displayName always null
data class ContactEmail(override val id: Long,
                        override val displayName: String?,
                        val email: String?) : BaseContact