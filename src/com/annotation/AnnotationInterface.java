package com.annotation;

public interface AnnotationInterface<T> {
    Class<? extends T> value();
}
