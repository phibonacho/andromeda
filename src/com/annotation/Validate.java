package com.annotation;

import com.annotation.types.AbstractValidateType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Validate {
    public Class<? extends AbstractValidateType> value();
    public boolean mandatory() default false;
    public String[] orAtLeast() default {};
}
