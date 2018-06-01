Core Rx Contacts
============

RxContactsProvider
------------------

This class provides rx api to work with phone contacts.

Instead of work with specific contact tables use the following abstractions:
 - ```UserContact``` to work with ```ContactsContract.Contacts```
 - ```ContactPhone``` to work with ```ContactsContract.CommonDataKinds.Phone```
 - ```ContactEmail``` to work with ```ContactsContract.CommonDataKinds.Email```

To understand how this abstractions depends look to this simplified scheme
```
   Contacts table (represents by UserContacts abstraction)
   ---------------------------------------------------------
  |_ID | LOOKUP_KEY | DISPLAY_NAME | STARRED | ... etc      |
   ---------------------------------------------------------
  | 1  | abcdef     | John Doe     |    1    | ...          |
   ---------------------------------------------------------
 
    Data table (represents by ContactPhone or ContactEmail abstraction)
   ---------------------------------------------------------------
  |_ID | CONTACT_ID | MIMETYPE     | DATA1          | ... etc      |
   ---------------------------------------------------------------
  | 32  |     1     | PHONE NUMBER | 555-000-1111   | ...          |
   ---------------------------------------------------------------
  | 33  |     1     | EMAIL        | some@gmail.com | ...          |
   ---------------------------------------------------------------
 
```
Provide methods to query contacts by params declared in ```QueryProperty```,
 or by specified uri.
 
Example usage:
```kotlin
RxContactsProvider.with(context)
          .query(UserContact::class.java)
          .where(QueryProperties.USER_NAME)
          .isLike("User")
          .fetchAll()
```
Or:
```kotlin
RxContactsProvider.with(context)
          .fromUri(UserContact::class.java, contactUri)
```

For full list of functions, please look to the source code,[example project](https://github.com/nullgr/app-core/tree/develop/app/src/main/java/com/nullgr/androidcore/rxcontacts)
or view documentation
