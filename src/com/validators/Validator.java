package com.validators;

import com.annotation.Validate;
import com.annotation.exception.ConflictFieldException;
import com.annotation.exception.InvalidFieldException;
import com.annotation.types.ValidateTypeInterface;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class Validator<Target> extends AbstractValidator <Target, Boolean> {
    public Validator(Target t) {
        super(t);
    }

    @Override
    public Boolean validate() throws ConflictFieldException, InvalidFieldException{

        return Arrays.stream(this.t.getClass().getDeclaredMethods())
                .filter(m -> m.getAnnotation(Validate.class)!=null)
                // getters has no param
                .filter(m -> m.getParameterCount() == 0)
                // invoke validate and obtain result
                .map(invokeAndHandle(m -> validateAnnotatedMethod(m.getAnnotation(Validate.class), m), (m) -> !m.getAnnotation(Validate.class).mandatory() || validateAlternatives(m)))
                // lazy find first false validation
                .filter(bool -> !bool).findFirst().orElse(true);
    }

    @SuppressWarnings("unchecked") // giusto perché mi fa schifo vederlo tutto giallo...
    private Boolean validateMethod(Validate v, Method paramGetter) throws Exception {
        return ValidateTypeInterface.create(v.with()).validate(paramGetter.invoke(this.t));
    }

    private Boolean validateAnnotatedMethod(Validate v, Method paramGetter) throws Exception {
        return validateMethod(v, paramGetter) && checkConflicts(paramGetter);
    }

    private boolean validateAlternatives(Method method) {
        Validate ann = method.getAnnotation(Validate.class);

        if(ann.mandatory() && ann.alternatives().length == 0) // controlla alternative
            throw new InvalidFieldException(method);

        return Arrays.stream(ann.alternatives())
                .map(checkMethod(mName ->  this.t.getClass().getDeclaredMethod(mName)))
                .map(invokeAndHandle(m -> validateMethod(ann, m), () -> { throw new InvalidFieldException(method, List.of(ann.alternatives()));}))
                // lazy find first not valid
                .filter(valid -> !valid)
                .findFirst().orElse(true);// valido le alternative
    }

    @FunctionalInterface
    private interface ThrowingFunction<I, R, E extends Exception> {
        R accept(I s) throws E;
    }

    @SuppressWarnings("unchecked")
    // invoke method and return or fallback if method invocation return null
    private <I,R> Function<I, R> invokeAndHandle(ThrowingFunction<I, R, Exception> throwingFunction, Function<I, R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                return fallback.apply(i);
            } catch (InvalidPropertiesFormatException e) {
                throw new InvalidFieldException(e.getMessage());
            } catch (ConflictFieldException e) {
                throw new ConflictFieldException(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private <I,R>Function<I, R> invokeAndHandle(ThrowingFunction<I, R, Exception> throwingFunction, Supplier<R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                return fallback.get();
            } catch (InvalidPropertiesFormatException e) {
                throw new InvalidFieldException(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    // return method invocation if method present
    private Function<? super String, Method> checkMethod(ThrowingFunction<String, Method, NoSuchMethodException> throwingFunction) {

        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NoSuchMethodException nsme) {
                throw new RuntimeException("cannot find method: " + nsme.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
    }

    // return true if no conflicts detected ()
    private Boolean checkConflicts(Method m) throws Exception{
        // verifica che ogni metodo che va in conflitto sia null, altrimenti lancia eccezione.
        Validate ann = m.getAnnotation(Validate.class);
        if(List.of(ann.conflicts())
                .stream()
                .map(checkMethod(mName -> this.t.getClass().getDeclaredMethod(mName)))
                .map(invokeAndHandle(method -> validateMethod(ann, method), () -> false))
                .filter(valid -> valid)
                .findFirst().orElse(false)) throw new ConflictFieldException(m, List.of(ann.conflicts()));
        return true;
    }

}
