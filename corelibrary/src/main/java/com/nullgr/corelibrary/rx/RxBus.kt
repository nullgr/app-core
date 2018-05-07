package com.nullgr.corelibrary.rx

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.nullgr.corelibrary.rx.relay.SingleSubscriberRelay
import io.reactivex.Observable

/**
 * Simple ***EVENT BUS*** built in a reactive manner. Provides publish-subscribe-style communication between components.
 * Can be used in two ways: with key or not. For each new key new [Relay] will be created.
 * Without key all events will be posted in in general [Relay]. Usage of this class is thread safe.
 * Use key based relays to split logic in your application. For correct work of bus, you need to post events and subscribe
 * to receive them, on the same instance of [RxBus]. The best usage is ***Singleton*** pattern.
 * You can implement singleton bus provider by yourself, or use our [SingletonRxBusProvider]
 *
 * <h2>Posting Events:</h2>
 * ```
 * SingletonRxBusProvider.BUS.post(event = SomeEventClass())
 * ```
 * or
 * ```
 * SingletonRxBusProvider.BUS.post(TypeOfYourEvent::class.java, SomeEventClass()) // you can use any type/class/object for event type
 * ```
 * <h2>Receiving Events:</h2>
 * ```
 * SingletonRxBusProvider.BUS.observable()
 *          .subscribe{// your code here}
 * ```
 * or
 * ```
 * SingletonRxBusProvider.BUS.observable(TypeOfYourEvent::class.java)
 *          .subscribe{// your code here}
 * ```
 * @author chernyshov.
 */
class RxBus {

    private val relayToTypeMap: HashMap<Any, Relay<Any>> by lazy {
        hashMapOf<Any, Relay<Any>>().apply {
            put(KEYS.GENERAL, PublishRelay.create<Any>().toSerialized())
            put(KEYS.SINGLE, SingleSubscriberRelay.create<Any>().toSerialized())
        }
    }

    /**
     * Posts an event to all registered subscribers.
     *
     * @param event [Any] event to post.
     * @param key key to get specific [Relay] in which event will be posted.
     * If null will be passed - event will be posted in general relay
     */
    @JvmOverloads
    fun post(key: Any = KEYS.GENERAL, event: Any) {
        getOrCreateRelay(key).asConsumer().accept(event)
    }

    /**
     * Returns an [Observable] of specific [Relay]
     *
     * @param key key to get specific [Relay].
     * If null will be passed - general [Relay] will be used
     */
    @JvmOverloads
    fun observable(key: Any = KEYS.GENERAL): Observable<Any> {
        return getOrCreateRelay(key).asObservable()
    }

    private fun getOrCreateRelay(type: Any): Relay<Any> {
        if (relayToTypeMap[type] == null) {
            relayToTypeMap[type] = PublishRelay.create<Any>().toSerialized()
        }
        return relayToTypeMap[type]!!
    }

    /**
     * Enum set of default keys for RxBus.
     */
    enum class KEYS {
        GENERAL, SINGLE
    }
}

/**
 * Provides an single instance of [RxBus]
 */
object SingletonRxBusProvider {
    val BUS = RxBus()
}