package com.nullgr.corelibrary.rx

import com.jakewharton.rxrelay2.PublishRelay

/**
 * @author chernyshov.
 */
class RxBus {

    private val clickRelay: PublishRelay<Any> = PublishRelay.create()

    private val eventsRelay: PublishRelay<Any> = PublishRelay.create()

    val clicksConsumer get() = clickRelay.asConsumer()

    val clicksObservable get() = clickRelay.asObservable()

    val eventsConsumer get() = eventsRelay.asConsumer()

    val eventsObservable get() = eventsRelay.asObservable()
}

object SingletonRxBusProvider {
    val BUS = RxBus()
}