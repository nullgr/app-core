package com.nullgr.core.rx.contacts.engine.query

import android.provider.ContactsContract
import com.nullgr.core.rx.contacts.domain.BaseContact
import com.nullgr.core.rx.contacts.domain.ContactEmail
import com.nullgr.core.rx.contacts.domain.ContactPhone
import com.nullgr.core.rx.contacts.domain.UserContact


/**
 * Enum of contact's generalized properties, used to build selection query for specific type of contacts data.
 * Every property will be mapped to one of column names listed in [ContactsContract].
 * This class was designed to simplify query building logic.
 * Note that not all properties are available for all child of [BaseContact].
 *
 * @author Grishko Nikita
 */
enum class QueryProperty {
    /**
     * Representation of **_ID** column. Available to all type of contacts.
     * - For [UserContact] will be mapped to [ContactsContract.Contacts._ID]
     * - For [ContactEmail] will be mapped to [ContactsContract.CommonDataKinds.Email._ID]
     * - For [ContactPhone] will be mapped to [ContactsContract.CommonDataKinds.Phone._ID]
     */
    ID,
    /**
     * Representation of **contact_id** column.
     * But for [UserContact] will be the same as for [ID] (as back compatibility)
     * Available to all type of contacts.
     * - For [UserContact] will be mapped to [ContactsContract.Contacts._ID]
     * - For [ContactEmail] will be mapped to [ContactsContract.CommonDataKinds.Email.CONTACT_ID]
     * - For [ContactPhone] will be mapped to [ContactsContract.CommonDataKinds.Phone.CONTACT_ID]
     */
    USER_CONTACT_ID,
    /**
     * Representation of **display_name** column.
     * Available to all type of contacts.
     * - For [UserContact] will be mapped to [ContactsContract.Contacts.DISPLAY_NAME]
     * - For [ContactEmail] will be mapped to [ContactsContract.CommonDataKinds.Email.DISPLAY_NAME]
     * - For [ContactPhone] will be mapped to [ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME]
     */
    USER_NAME,
    /**
     * Representation of **NUMBER** (or more specifically **data1**) column.
     * Can be used for [UserContact] and for [ContactPhone].
     * - For [UserContact] will be mapped to [ContactsContract.CommonDataKinds.Phone.NUMBER]
     * - For [ContactPhone] will be mapped to [ContactsContract.CommonDataKinds.Phone.NUMBER]
     * - For [ContactEmail] not available([IllegalArgumentException] will be thrown)
     */
    PHONE,
    /**
     * Representation of **NORMALIZED_NUMBER** (or more specifically **data4**)  column.
     * Can be used for [UserContact] and for [ContactPhone].
     * - For [UserContact] will be mapped to [ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER]
     * - For [ContactPhone] will be mapped to [ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER]
     * - For [ContactEmail] not available ([IllegalArgumentException] will be thrown)
     */
    NORMALIZED_PHONE,
    /**
     * Representation of **EMAIL** (or more specifically **data1**)  column.
     * Can be used for [UserContact] and for [ContactEmail].
     * - For [UserContact] will be mapped to [ContactsContract.CommonDataKinds.Email.DATA1]
     * - For [ContactPhone] not available ([IllegalArgumentException] will be thrown)
     * - For [ContactEmail] will be mapped to [ContactsContract.CommonDataKinds.Email.DATA1]
     */
    EMAIL,
    /**
     * Representation of **starred** column.
     * Available to all type of contacts.
     * - For [UserContact] will be mapped to [ContactsContract.Contacts.STARRED]
     * - For [ContactPhone] will be mapped to [ContactsContract.CommonDataKinds.Phone.STARRED]
     * - For [ContactEmail] will be mapped to [ContactsContract.CommonDataKinds.Email.STARRED]
     */
    FAVORITES
}