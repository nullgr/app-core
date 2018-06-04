Core Rx
============

This module contains different extensions and utilities for RxJava2 

RxExtensions
-----------
Contains extension functions for RxJava2.

Sample:
```kotlin
 getSuggestedFriendsUseCase.execute(EXAMPLE_ID)
                            .zipWithTimer(DELAY) 
                            .bindProgress(progressConsumer)
                            .applySchedulers(IoToMainSchedulersFacede())
                            .doOnSuccess { toast(it.toString()) }
                            .toObservable()
```
Schedulers
----------
Contains classes that implements ```SchedulersFacade``` to work with basic schedulers,
such as ```Schedulers.computation()```, ```Schedulers.io()```, ```AndroidSchedulers.mainThread()```.
**SchedulersFacade** - helper interface that provides access of client code to ```Scheduler```s.
Contains properties ```subscribeOn``` and ```observeOn``` that can be used with reactive data sources:
```Observable```, ```Flowable```, ```Single``` and ```Completable```.

This class designed to work with one of ```applySchedulers(schedulersFacade: SchedulersFacade)```functions,
which implemented in ```RxExtensions```

RxBus
------
Simple ***EVENT BUS*** built in a reactive manner. Provides publish-subscribe-style communication between components.
Can be used in two ways: with key or not. For each new key new ```Relay``` will be created.
Without key all events will be posted in in general ```Relay```. The same behaviour if use ```Keys.GENERAL```.
Usage of this class is thread safe.

Use key based relays to split logic in your application.
Or you can use keys specified in ``Keys`` to access some specific relays, provided with this class.
To get more information about this relays read documentation of ```Keys```

For correct work of bus, you need to post events and subscribe
to receive them, on the same instance of ```RxBus```. The best usage is ***Singleton*** pattern.
You can implement singleton bus provider by yourself, or use our ```SingletonRxBusProvider```

**Posting Events:**
```kotlin
  SingletonRxBusProvider.BUS.post(event = SomeEventClass())
```
or
```kotlin
SingletonRxBusProvider.BUS.post(TypeOfYourEvent::class.java, SomeEventClass()) // you can use any type/class/object for event type
```
or
```kotlin
SingletonRxBusProvider.BUS.post(RxBus.Keys.SINGLE, SomeEventClass()) // to use SingleSubscriberRelay as events bus
```
**Receiving Events**
```kotlin
 SingletonRxBusProvider.BUS.observable()
         .subscribe{// your code here}
```
 or
```kotlin
 SingletonRxBusProvider.BUS.observable(TypeOfYourEvent::class.java)
         .subscribe{// your code here}
```

For full list of functions, please look to the source code,[example project](https://github.com/nullgr/app-core/tree/develop/app/src/main/java/com/nullgr/androidcore)
or view documentation

