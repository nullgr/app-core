package com.nullgr.core.rx.contacts.engine.query

/**
 * Inner helper to build selection argument string
 *
 * @author Grishko Nikita
 */
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