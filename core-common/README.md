Core Common
============

SdkVersion
----------
A simple class for defining branching depending on the current version of the SDK.
SdkVersion api designed in DSL style.

***How to use:***

```kotlin

 withVersion(Build.VERSION_CODES.LOLLIPOP) {
        higherOrEqual {
            doSomeThing()
        }
        lower {
           doSomeThingElse()
        }
    }
```

Also contains methods
```kotlin
 inline fun higher(function: () -> Unit): SdkVersion
 inline fun lowerOrEqual(function: () -> Unit): SdkVersion
 inline fun equal(function: () -> Unit): SdkVersion
```