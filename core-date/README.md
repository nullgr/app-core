Core Date
============

DateFormatExtensions
--------------------
A set of extension functions to convert ```Date``` to ```String``` in both way.
Also contains ```CommonFormats``` singleton object which contains the most commonly used date formats

Sample usage:
```kotlin
 val stringDate = Date().toStringWithFormat(CommonFormats.FORMAT_SIMPLE_DATE)
``` 
And in the opposite direction
```kotlin
val date = stringDate.toDate(CommonFormats.FORMAT_SIMPLE_DATE)
```
Note: SimpleDateFormatter objects will be created once and cached by 
params of methods [dateFormat], [timeZone] and [locale]. 

There is also an alternative version:
```kotlin
val simpleDateFormat = SimpleDateFormat(CommonFormats.FORMAT_SIMPLE_DATE, Locale.getDefault())
val stringDate = Date().toStringWithFormat(simpleDateFormat)
``` 
And in the opposite direction
```kotlin
val simpleDateFormat = SimpleDateFormat(CommonFormats.FORMAT_SIMPLE_DATE, Locale.getDefault())
val date = stringDate.toDate(simpleDateFormat)
```



DateHelperExtensions
--------------------
A set of extension functions to work with ```Date```
Such as
```kotlin
val date = Date()
val result:Boolean = date.isToday()
val result:Boolean = date.isYesterday()
val newDate:Date = date plusMonth 1
```
For full list of methods, please look to the source code, [example project](https://github.com/nullgr/app-core/tree/develop/app/src/main/java/com/nullgr/androidcore/date)
or view documentation