Core Preferences
============

Contains a set of extensions and factory functions to work with ```SharedPreferences```

* Access to default preferences
```kotlin
val prefs = defultPrefs(context)
```
* Or to custom preferences
```kotlin
val customPrefs = customPrefs(context,"MyPreferences")
```
* Extensions to set and get primitive types values in ```SharedPreferences```
```kotlin
val prefs = defultPrefs(context)
prefs[SOME_KEY] = "SomeValue"
val string = prefs[SOME_KEY]
```
For full list of functions, please look to the source code, or view documentation