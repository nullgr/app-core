Core Rx Location
============

RxLocationProvider
-----------------
Simple facade above [ReactiveLocationProvider](https://github.com/mcharmas/Android-ReactiveLocation) 
to receive location - both LastKnown and Updated.
Also it checks and handle location settings state, and process result without overriding and handling
```onAcitivtyResult``` 

Sample usage:
```kotlin
val rxLocationManager = RxLocationManager(this, 1100, 10)
rxLocationManager.requestLocation()
                    .subscribe {
                        locationLog.text = if (!it.isEmpty()) {
                            getString(R.string.mask_location_result, ++updatesCount, it.toString())
                        } else {
                            getString(R.string.error_empty_location)
                        }
                    }
```

For full list of functions, please look to the source code,[example project](../app/src/main/java/com/nullgr/androidcore/location)
or view documentation