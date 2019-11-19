package com.annotation;

import com.annotation.types.AbstractValidateType;

public interface AnnotationInterface {
    Class<? extends AbstractValidateType> value();
}
