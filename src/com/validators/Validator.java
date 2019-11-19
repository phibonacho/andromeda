package com.validators;

import com.annotation.Validate;
import com.sun.xml.bind.v2.ClassFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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

    @FunctionalInterface
    private interface ThrowingFunction<Target, Source, E extends Exception> {
        Target accept(Source t) throws E;
    }

    private static <Source,Target>Function<? super Source, ? extends Target> throwingFunctionWrapper(
            ThrowingFunction<Target, Source, Exception> throwingConsumer) {

        return i -> {
            try {
                return throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    public Boolean validate() throws IllegalArgumentException, InstantiationError {
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
        } catch (Exception npe) {
            throw new RuntimeException("couldn't validate data: " + npe.getMessage());
        }
    }

    @SuppressWarnings("unchecked") // giusto perché mi fa schifo vederlo tutto giallo...
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
