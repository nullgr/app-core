package com.nullgr.core.rx

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.nullgr.core.rx.RxBus.Keys
import com.nullgr.core.rx.RxBus.Keys.GENERAL
import com.nullgr.core.rx.RxBus.Keys.SINGLE
import com.nullgr.core.rx.relay.SingleSubscriberRelay
import io.reactivex.Observable

/**
 * Simple ***EVENT BUS*** built in a reactive manner. Provides publish-subscribe-style communication between components.
 * Can be used in two ways: with key or not. For each new key new [Relay] will be created.
 * Without key all events will be posted in in general [Relay]. The same behaviour if use [Keys.GENERAL].
 * Usage of this class is thread safe.
 *
 * Use key based relays to split logic in your application.
 * Or you can use keys specified in [Keys] to access some specific relays, provided with this class.
 * To get more information about this relays read documentation of [Keys]
 *
 * For correct work of bus, you need to post events and subscribe
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
 * or
 * ```
 * SingletonRxBusProvider.BUS.post(RxBus.Keys.SINGLE, SomeEventClass()) // to use SingleSubscriberRelay as events bus
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
 * @author chernyshov, Grishko Nikita
 */
class RxBus {

    private val relayToTypeMap: HashMap<Any, Relay<Any>> by lazy {
        hashMapOf<Any, Relay<Any>>().apply {
            put(Keys.GENERAL, PublishRelay.create<Any>().toSerialized())
            put(Keys.SINGLE, SingleSubscriberRelay.create<Any>().toSerialized())
        }
    }

    /**
     * Posts an event to all registered subscribers.
     *
     * @param event [Any] event to post.
     * @param key key to get specific [Relay] in which event will be posted.
     * Default key is [Keys.GENERAL].
     */
    fun post(key: Any, event: Any) {
        getOrCreateRelay(key).asConsumer().accept(event)
    }

    /**
     * Posts an event to all registered subscribers.
     * Event will be posted in relay with [GENERAL] key
     *
     * @param event [Any] event to post.
     * Default key is [Keys.GENERAL].
     */
    fun post(event: Any) {
        post(Keys.GENERAL, event)
    }

    /**
     * Returns an [Observable] of specific [Relay]
     *
     * @param key key to get specific [Relay].
     * Default key is [Keys.GENERAL].
     */
    @JvmOverloads
    fun observable(key: Any = Keys.GENERAL): Observable<Any> {
        return getOrCreateRelay(key).asObservable()
    }

    private fun getOrCreateRelay(type: Any): Relay<Any> {
        if (relayToTypeMap[type] == null) {
            relayToTypeMap[type] = PublishRelay.create<Any>().toSerialized()
        }
        return relayToTypeMap[type]!!
    }

    /**
     * Enum set of default keys for RxBus. Every key associated with specific [Relay]
     * - [GENERAL] - provides access to general [PublishRelay] which used as events bus
     * - [SINGLE] - provides access to [SingleSubscriberRelay] which used as events bus.
     * The main feature of this relay is that it always provides communication with only one subscriber.
     * Any new subscriber will automatically overrides previous
     */
    enum class Keys {
        /**
         * Provides access to general [PublishRelay] which used as events bus
         */
        GENERAL,
        /**
         * Provides access to [SingleSubscriberRelay] which used as events bus.
         * The main feature of this relay is that it always provides communication with only one subscriber.
         * Any new subscriber will automatically overrides previous
         */
        SINGLE
    }
}

/**
 * Provides an single instance of [RxBus]
 */
object SingletonRxBusProvider {
    val BUS = RxBus()
}