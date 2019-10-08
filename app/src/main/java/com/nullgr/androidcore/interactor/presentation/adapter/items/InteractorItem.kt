package com.nullgr.androidcore.interactor.presentation.adapter.items

import com.nullgr.core.adapter.items.ListItem

/**
 * @author chernyshov.
 */
data class InteractorItem(
    val type: Type,
    val title: String,
    override val uniqueProperty: Any = type
) : ListItem {

    enum class Type {
        OBSERVABLE, OBSERVABLE_LIST,
        FLOWABLE, FLOWABLE_LIST,
        SINGLE, SINGLE_LIST,
        COMPLETABLE
    }
}