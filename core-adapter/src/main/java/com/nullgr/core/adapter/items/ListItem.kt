package com.nullgr.core.adapter.items

/**
 * Base interface for all items that can be used in [com.nullgr.core.adapter.DynamicAdapter].
 * Contains method needed for [com.nullgr.core.adapter.Callback].
 *
 * @author vchernyshov
 * @author a.komarovskyi
 */
interface ListItem {

    /**
     * Used in [areItemsTheSame] as an extra check for item uniqueness.
     * You should return some unique property (id that won't change) for this item.
     */
    val uniqueProperty: Any

    /**
     * Called by the [com.nullgr.core.adapter.Callback] to decide whether two object represent the same item.
     * Method makes check by two parameters: class of item and [uniqueProperty].
     * In most cases you should not override this method and use default implementation.
     */
    fun areItemsTheSame(other: ListItem): Boolean {
        return this::class == other::class && this.uniqueProperty == other.uniqueProperty
    }

    /**
     * Called by the [com.nullgr.core.adapter.Callback] when it wants to check whether two items have the same data.
     * By default method checks equality using [Any.equals].
     * It is a good idea to use Kotlin data classes as items.
     * If you using Java you should specify equals method or override this.
     */
    fun areContentsTheSame(other: ListItem): Boolean {
        return this == other
    }

    /**
     * Called by the [com.nullgr.core.adapter.Callback] to get info about particular changes in the item.
     * If item supports changes of few properties at same time return set of payloads to prevent many same changes and handle all changes at UI.
     */
    fun getChangePayload(other: ListItem): Any {
        return Unit
    }
}