package com.nullgr.androidcore.adapter

import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * @author chernyshov.
 */
sealed class Event {
    class Click(val item: ListItem) : Event()
    class Payload(val payload: Any): Event()
}