Core Hardware
============
A set of extensions to check some hardware features

NetworkChecker
--------------
Class that provides API to check current network status.

Sample usage:
```kotlin
val networkChecker = NetworkChecker(context)
val internetStatus = networkChecker.isInternetConnectionEnabled()
val wifiStatus = networkChecker.isConnectedOverWifi()
```

SupportedFeaturesExtensions
---------------------------
Sample usage:

```kotlin
val isGpsSupported = context.isGpsSupported()
val isFingerprintSupported = context.isFingerpringSupported()
```

For full list of functions, please look to the source code, or view documentation