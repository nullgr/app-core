Core Date
============

DateFormatExtensions
--------------------
A set of extension functions to convert ```ZonedDateTime``` to ```String``` in both way.
Also contains ```CommonFormats``` singleton object which contains the most commonly used date formats

Sample usage:
```kotlin
 val stringDate = ZonedDateTime.now().toStringWithFormat(CommonFormats.FORMAT_SIMPLE_DATE)
``` 
And in the opposite direction
```kotlin
val date = stringDate.toDate(CommonFormats.FORMAT_SIMPLE_DATE)
```
Note: DateTimeFormatter objects will be created once and cached by 
params of methods [dateFormat] and [locale]. 

There is also an alternative version:
```kotlin
val dateFormat = DateTimeFormatter.ofPattern(CommonFormats.FORMAT_SIMPLE_DATE, Locale.getDefault())
val stringDate = ZonedDateTime.now().toStringWithFormat(dateFormat)
``` 
And in the opposite direction
```kotlin
val dateFormat = DateTimeFormatter.ofPattern(CommonFormats.FORMAT_SIMPLE_DATE, Locale.getDefault())
val date = stringDate.toDate(dateFormat)
```



DateHelperExtensions
--------------------
A set of extension functions to work with ```Date```
Such as
```kotlin
val date = ZonedDateTime.now()
val result: Boolean = date.isToday()
val result: Boolean = date.isYesterday()
```
For full list of methods, please look to the source code, [example project](../app/src/main/java/com/nullgr/androidcore/date)
or view documentation