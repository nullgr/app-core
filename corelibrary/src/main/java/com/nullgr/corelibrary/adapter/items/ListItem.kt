package com.nullgr.corelibrary.adapter.items

/**
 * Base interface for all items that can be used in [com.nullgr.corelibrary.adapter.DynamicAdapter].
 * Contains method needed for [com.nullgr.corelibrary.adapter.Callback].
 *
 * @author vchernyshov
 * @author a.komarovskyi
 */
interface ListItem {

    /**
     * Called by the [com.nullgr.corelibrary.adapter.Callback] to decide whether two object represent the same item.
     * Method makes check by two parameters: class of item and unique property returned by [getUniqueProperty].
     * In most cases you should not override this method and use default implementation.
     */
    fun areItemsTheSame(other: ListItem): Boolean {
        return this::class == other::class && this.getUniqueProperty() == other.getUniqueProperty()
    }

    /**
     * Called by the [com.nullgr.corelibrary.adapter.Callback] when it wants to check whether two items have the same data.
     * By default method checks equality using [Any.equals].
     * It is a good idea to use Kotlin data classes as items.
     * If you using Java you should specify equals method or override this.
     */
    fun areContentsTheSame(other: ListItem): Boolean {
        return this == other
    }

    /**
     * Called by the [com.nullgr.corelibrary.adapter.Callback] to get info about particular changes in the item.
     * If item supports changes of few properties at same time return set of payloads to prevent many same changes and handle all changes at UI.
     */
    fun getChangePayload(other: ListItem): Any {
        return Unit
    }

    /**
     * Called in [areItemsTheSame] as a extra check for item uniquely.
     * By default returns string representation of item class.
     * If you list contains multiple items of same class you should override this method and return some unique property.
     */
    fun getUniqueProperty(): Any {
        return this::class.toString()
    }
}