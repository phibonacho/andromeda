package com.validators;

import java.lang.reflect.InvocationTargetException;
import java.util.InvalidPropertiesFormatException;

public abstract class AbstractValidator <Target, Control> {
    protected Target t;

    public AbstractValidator(Target t){
        this.t = t;
    }

    public abstract Control validate() throws Exception;
}