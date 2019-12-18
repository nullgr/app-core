package com.nullgr.core.rx.contacts.engine.query

import android.provider.ContactsContract
import androidx.annotation.VisibleForTesting
import com.nullgr.core.rx.contacts.domain.ContactEmail
import com.nullgr.core.rx.contacts.domain.ContactPhone
import com.nullgr.core.rx.contacts.domain.UserContact

/**
 * Inner helper for mapping [QueryProperty] to one of column names
 *
 * @author Grishko Nikita
 */
@VisibleForTesting
internal object PropertyToColumnNameMapper {

    fun map(property: QueryProperty, clazz: Class<*>): String {
        return when (clazz) {
            UserContact::class.java -> userContactPropertyToColumnName(property)
            ContactPhone::class.java -> phoneContactPropertyToColumnName(property)
            ContactEmail::class.java -> emailContactPropertyToColumnName(property)
            else -> throw IllegalArgumentException("Unknown class $clazz")
        }
    }

    private fun userContactPropertyToColumnName(property: QueryProperty): String =
            when (property) {
                QueryProperty.ID -> ContactsContract.Contacts._ID
                QueryProperty.USER_CONTACT_ID -> ContactsContract.Contacts._ID
                QueryProperty.USER_NAME -> ContactsContract.Contacts.DISPLAY_NAME
                QueryProperty.FAVORITES -> ContactsContract.Contacts.STARRED
                QueryProperty.NORMALIZED_PHONE -> ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
                QueryProperty.PHONE -> ContactsContract.CommonDataKinds.Phone.NUMBER
                QueryProperty.EMAIL -> ContactsContract.CommonDataKinds.Email.DATA1
            }


    private fun emailContactPropertyToColumnName(property: QueryProperty): String =
            when (property) {
                QueryProperty.ID -> ContactsContract.CommonDataKinds.Email._ID
                QueryProperty.USER_CONTACT_ID -> ContactsContract.CommonDataKinds.Email.CONTACT_ID
                QueryProperty.USER_NAME -> ContactsContract.Data.DISPLAY_NAME
                QueryProperty.FAVORITES -> ContactsContract.CommonDataKinds.Email.STARRED
                QueryProperty.EMAIL -> ContactsContract.CommonDataKinds.Email.DATA1
                else -> throw IllegalArgumentException("Not valid property: $property " +
                        "for class ${ContactEmail::class.java}")
            }

    private fun phoneContactPropertyToColumnName(property: QueryProperty): String =
            when (property) {
                QueryProperty.ID -> ContactsContract.CommonDataKinds.Phone._ID
                QueryProperty.USER_CONTACT_ID -> ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                QueryProperty.USER_NAME -> ContactsContract.Contacts.DISPLAY_NAME
                QueryProperty.FAVORITES -> ContactsContract.Contacts.STARRED
                QueryProperty.NORMALIZED_PHONE -> ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
                QueryProperty.PHONE -> ContactsContract.CommonDataKinds.Phone.NUMBER
                else -> throw IllegalArgumentException("Not valid property: $property " +
                        "for class ${ContactPhone::class.java}")
            }
}