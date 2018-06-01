Core Resources
============

ResourceProvider
--------------
Resource provider serves to provide any resource from context.
Best practice is to use singleton instance provided with DI.
 
Sample usage:
 
```kotlin
resourceProvder.getString(R.string.some_string)
```

For full list of functions, please look to the source code, or view documentation