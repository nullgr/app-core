package com.nullgr.corelibrary.rxcontacts.engine.query

import android.support.annotation.VisibleForTesting

/**
 * Inner helper to build selection argument string
 *
 * @author Grishko Nikita
 */
@VisibleForTesting
internal object SelectionArgsBuilder {

    fun buildWhere(selectionArg: String): String {
        return " = '$selectionArg'"
    }

    fun buildIn(selectionArgs: Array<out String>): String {
        if (selectionArgs.size == 1) {
            return buildWhere(selectionArgs[0])
        }
        return selectionArgs.joinToString(separator = ",",
                prefix = " IN (",
                postfix = ")",
                transform = { "'$it'" })
    }

    fun buildLike(selectionArg: String): String {
        return " LIKE '%$selectionArg%'"
    }
}