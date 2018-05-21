package com.nullgr.androidcore.adapter

/**
 * @author chernyshov.
 */
sealed class Event {
    class Click(val item: Any) : Event()
    class Payload(val payload: Any): Event()
}