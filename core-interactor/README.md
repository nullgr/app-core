Core Interactor
============

This module contains set of abstract classes that represents Use Case (Interactor in terms of Clean Architecture).
This interface represents a execution unit for different use cases (this means any use case
in the application should implement this contract). 
All of this classes adopted to work with [RxJava](https://github.com/ReactiveX/RxJava/) entities, such as ```Observable```, ```Single```,
```Flowable``` and ```Completable```. Also, for each entity, has two variants: one of them returns singe item,
and second returns ```List``` of some items.

List of represented classes:
---------------------------
* CompletableUseCase
* FlowableListUseCase
* FlowableUseCase
* ObservableUseCase
* ObservableListUseCase
* SingleListUseCase
* SingleUseCase

View[sample project](../app/src/main/java/com/nullgr/androidcore/interactor)for implementation guide.