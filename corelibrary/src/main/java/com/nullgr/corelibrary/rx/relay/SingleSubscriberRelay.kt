package com.nullgr.corelibrary.rx.relay

import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * Relay that can has only one subscriber. Any new subscription will replace previous.
 * Originally based on [com.jakewharton.rxrelay2.PublishRelay]
 *
 * Example usage:
 * ```
 * var relay = SingleSubscriberRelay.create<Any>()
 * // observer1 will receive  1 and 2 events
 * relay.subscribe(observer1)
 * relay.accept("1")
 * relay.accept("2")
 * // observer2 will receive  3 and 4 events (observer1 will automatically disposed)
 * relay.subscribe(observer2)
 * relay.accept("3")
 * relay.accept("4")
 * ```
 */
class SingleSubscriberRelay<T> private constructor() : Relay<T>() {

    companion object {
        /**
         * Constructs a new SingleSubscriberRelay.
         */
        fun <T> create(): SingleSubscriberRelay<T> {
            return SingleSubscriberRelay()
        }
    }

    /**
     * Currently subscribed subscriber.
     */
    private val subscriber: AtomicReference<PublishDisposable<T>> = AtomicReference()

    override fun subscribeActual(t: Observer<in T>) {
        val ps = PublishDisposable(t, this)
        t.onSubscribe(ps)
        replace(ps)
        // if cancellation happened while a successful add, the remove() didn't work
        // so we need to do it again
        if (ps.isDisposed) {
            remove(ps)
        }
    }

    /**
     * Replace previous subscriber with new one
     * @param ps the subscriber to add
     */
    private fun replace(ps: PublishDisposable<T>) {
        while (true) {
            val prev = subscriber.get()
            if (subscriber.compareAndSet(prev, ps))
                return
        }
    }

    /**
     * Atomically removes the given subscriber if it is subscribed to the subject.
     * @param ps the subject to remove
     */
    internal fun remove(ps: PublishDisposable<T>) {
        while (true) {
            if (subscriber.compareAndSet(ps, null)) return
        }
    }

    override fun accept(value: T) {
        if (value == null) throw NullPointerException("value == null")
        if (hasObservers())
            subscriber.get().onNext(value)
    }

    override fun hasObservers(): Boolean {
        return subscriber.get() != null
    }

    internal class PublishDisposable<T>(private val actual: Observer<in T>,
                                        val parent: SingleSubscriberRelay<T>)
        : AtomicBoolean(), Disposable {

        companion object {
            private const val serialVersionUID = 3562861878281475070L
        }

        fun onNext(t: T) {
            if (!get()) {
                actual.onNext(t)
            }
        }

        override fun dispose() {
            if (compareAndSet(false, true)) {
                parent.remove(this)
            }
        }

        override fun isDisposed(): Boolean {
            return get()
        }
    }
}
