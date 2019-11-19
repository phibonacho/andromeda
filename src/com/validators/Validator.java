package com.validators;

import com.annotation.Validate;
import com.sun.xml.bind.v2.ClassFactory;

import javax.validation.UnexpectedTypeException;
import java.lang.reflect.Method;
import java.rmi.UnexpectedException;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Objects;
import java.util.Optional;

public class Validator<Target> extends AbstractValidator <Target, Boolean> {
    private Exception exception;
    public Validator(Target t) {
        super(t);
    }

    private Object invokeWrapper(Method m, Object ...params) {
        try {
            return m.invoke(this.t, params);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean validate() throws Exception {
        try {
            if (!Arrays.stream(this.t.getClass().getDeclaredMethods())
                    .filter(m -> Optional.ofNullable(m.getAnnotation(Validate.class)).map(Validate::mandatory).orElse(false))
                    .filter(m -> m.getParameterCount() == 0)
                    .map(m -> invokeValidate(m.getAnnotation(Validate.class), m))
                    .filter(bool -> !bool).findFirst().orElse(true))
                throw exception;
            return true;
        } catch (InstantiationException ie) {
            throw new IllegalArgumentException("primitive annotation types cannot be used, implements a valid annotation");
        } catch (UnexpectedException npe) {
            throw new RuntimeException("couldn't validate data: " + npe.getMessage());
        } catch (NullPointerException npe) {
            throw new RuntimeException("couldn't validate data: " + npe.getMessage());
        }
    }

    private Boolean invokeValidate(Validate v, Method paramGetter) {
        try {
            return ClassFactory.create(v.value()).validate(invokeWrapper(paramGetter));
        } catch (InvalidPropertiesFormatException e) {
            exception = e;
        } catch (IllegalArgumentException iae) {
            exception = new IllegalArgumentException("[" + paramGetter.getReturnType().getName() + "] " + paramGetter.getName() + " cannot be validated by annotation [" + ClassFactory.create(v.value()).getClass() + "]");
        } catch (NullPointerException npe) {
            exception = new InvalidPropertiesFormatException(paramGetter.getName() + ", annotated with " + ClassFactory.create(v.value()).getClass() + " cannot return null");
        }
        return false;
    }
}
