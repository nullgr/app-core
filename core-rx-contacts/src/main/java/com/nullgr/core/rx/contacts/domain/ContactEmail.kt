package com.nullgr.core.rx.contacts.domain

/**
 * Created by Grishko Nikita on 01.02.18.
 */
data class ContactEmail(override val id: Long,
                        override val displayName: String?,
                        val isStarred:Boolean,
                        val email: String?) : BaseContact