package com.nullgr.corelibrary.collections;

/**
 * @author chernyshov.
 */
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}
