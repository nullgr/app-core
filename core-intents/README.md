Core Intents
============

CommonIntentsExtensions
-----------------------

A set of factory functions to quick create common intents, and extensions for ```Intent``` class

***Factory functions***
```kotlin
var intent = webIntent(url)
var intent = emailIntent(url)
var intent = callIntent(url)
```

***Extensions***
```kotlin
webIntent(url).launch(context)

Intent(SOME_ACTION).sendBroadcast(context)
```
Also contains method to ```launchForResult``` activity in Rx way

Sample usage:
```kotlin
selectContactIntent()
           .launchForResult(this)
           .subscribe({it:RxAcitivtyResult -> processResult(it)},
                   {handleError(it)})
```
For full list of functions, please look to the source code,[example project](https://github.com/nullgr/app-core/tree/develop/app/src/main/java/com/nullgr/androidcore/intents)
or view documentation