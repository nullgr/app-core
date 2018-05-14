package com.nullgr.androidcore.interactor.presentation.adapter.items

import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * @author chernyshov.
 */
data class InteractorItem(val type: Type, val title: String) : ListItem {

    enum class Type {
        OBSERVABLE, OBSERVABLE_LIST,
        FLOWABLE, FLOWABLE_LIST,
        SINGLE, SINGLE_LIST,
        COMPLETABLE
    }
}