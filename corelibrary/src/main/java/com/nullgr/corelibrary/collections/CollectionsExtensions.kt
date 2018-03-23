package com.nullgr.corelibrary.collections

import android.util.SparseArray

/**
 * Created by Grishko Nikita on 01.02.18.
 */
fun <T> Collection<T>?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

fun <T> MutableCollection<T>?.replace(source: MutableCollection<T>?) {
    source?.let {
        this?.clear()
        this?.addAll(it)
    }
}

fun <T> MutableCollection<T>?.addUnique(source: MutableCollection<T>?) {
    this?.let {
        source?.let {
            source.forEach { item ->
                if (item !in this) {
                    this.add(item)
                }
            }
        }
    }
}

fun <T> Iterable<T>.split(predicates: MutableCollection<Predicate<T>>): SparseArray<List<T>> {
    val destinations = SparseArray<List<T>>()
    for (element in this) {
        predicates.forEachIndexed { index, predicate ->
            if (destinations[index] == null) {
                destinations.put(index, arrayListOf())
            }
            val destination = destinations[index]
            if (predicate.test(element)) (destination as MutableList).add(element)
        }
    }
    return destinations
}