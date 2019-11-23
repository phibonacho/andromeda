package com.validators;

import com.annotation.Validate;
import com.sun.source.doctree.StartElementTree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Validator<Target> extends AbstractValidator <Target, Boolean> {
    private Exception exception;
    public Validator(Target t) {
        super(t);
    }

    @Override
    public Boolean validate() {
            return Arrays.stream(this.t.getClass().getDeclaredMethods())
                    .filter(m -> Optional.ofNullable(m.getAnnotation(Validate.class))
                                    .map(Validate::mandatory)
                                 .orElse(false))
                    .filter(m -> m.getParameterCount() == 0)
                    .map(m -> invokeValidate(m.getAnnotation(Validate.class), m))
                    .filter(bool -> !bool).findFirst().orElse(true);
    }

    @SuppressWarnings("unchecked") // giusto perché mi fa schifo vederlo tutto giallo...
    // questa funzione dovrebbe essere implementata come nella parte commentata, in modo da poter lanciare direttamente l'eccezione
    private Boolean invokeValidate(Validate v, Method paramGetter) {
        List<Method> orElse = Arrays.stream(v.orAtLeast()).map(mName -> {
            try {
                return this.t.getClass().getDeclaredMethod(mName);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        orElse.add(paramGetter);
        return orElse.stream()
                .map(invokeAndValidate(method -> v.value().getDeclaredConstructor().newInstance()
                        .validate(method.invoke(this.t))))
                .filter(valid -> !valid)
                .findFirst()
                .orElse(true);
    }

    @FunctionalInterface
    private interface ThrowingFunction<R, I, E extends Exception> {
        R accept(I s) throws E;
    }

    // troppo specifico...
    private static <T>Function<? super Method, T>  invokeAndValidate(ThrowingFunction<T, Method, Exception> throwingFunction) throws RuntimeException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

}
