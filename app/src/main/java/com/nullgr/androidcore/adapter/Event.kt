package com.nullgr.androidcore.adapter

import com.nullgr.core.adapter.items.ListItem

/**
 * @author chernyshov.
 */
sealed class Event {
    class Click(val item: Any) : Event()
    class Payload(val payload: Any): Event()
}