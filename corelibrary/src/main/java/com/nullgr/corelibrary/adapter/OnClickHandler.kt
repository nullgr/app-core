package com.nullgr.corelibrary.adapter

import android.view.View
import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * Interface for handling click on particular [ListItem].
 *
 * @author chernyshov.
 */
interface OnClickHandler {

    /**
     * Implement this method to handle click.
     *
     * @param view View that had been clicked.
     * @param item ListItem related to clicked [view].
     * @param position Position of [item].
     */
    fun handle(view: View, item: ListItem, position: Int)
}