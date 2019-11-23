package com.throwables;

public interface FunctionThrowable<T, R, E extends Exception> {
    R apply(T t) throws E;
}
