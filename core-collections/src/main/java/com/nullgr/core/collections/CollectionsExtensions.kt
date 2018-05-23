package com.nullgr.core.collections

import android.util.SparseArray

/**
 * Extension function to check if collection is null or empty
 */
fun <T> Collection<T>?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

/**
 * Extension function to check if collection is not null or empty
 */
fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

/**
 * Extension function to check if array is null or empty
 */
fun <T> Array<T>?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

/**
 * Extension function to check if array is not null or empty
 */
fun <T> Array<T>?.isNotNullOrEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

/**
 * Extension function to replace all elements
 * in destination collection with elements from [source] collection
 */
fun <T> MutableCollection<T>?.replace(source: Collection<T>?) {
    source?.let {
        this?.clear()
        this?.addAll(it)
    }
}

/**
 * Extension function to add all unique elements
 * to destination collection with elements from [source] collection
 */
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

/**
 * Extension function to split [Iterable] with given collection of [Predicate].
 * For each predicate, an [ArrayList] will be created which will contain
 * the items for which the [Predicate.test] returns a ***true*** value
 * @return [SparseArray] of [List].
 * Every sublist in [SparseArray] will have the same index as his [Predicate] in [predicates] collection.
 */
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