package com.nullgr.core.collections;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 *
 * @param <T> the type of the input to the predicate
 *
 * @author chernyshov
 */
public interface Predicate<T> {
    boolean test(T t);
}
