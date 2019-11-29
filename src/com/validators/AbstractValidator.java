package com.validators;

import com.annotation.exception.AnnotationException;
import com.annotation.exception.ConflictFieldException;
import com.annotation.exception.InvalidFieldException;
import com.annotation.exception.RequirementsException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InvalidPropertiesFormatException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class AbstractValidator <Target, Control, A extends Annotation> {
    protected Target t;
    protected Class<A> annotationClass;

    public AbstractValidator(Target t){
        this.t = t;
    }

    private Stream<Control> validateStream() {
        return Arrays.stream(this.t.getClass().getDeclaredMethods())
                .filter(m -> m.getAnnotation(annotationClass)!=null)
                // getters has no param
                .filter(m -> m.getParameterCount() == 0)
                .sorted(Comparator.comparing(sortPredicate())) // mandatory fields as first
                // invoke validate and obtain result
                .map(validateAlgorithm()); // check for viable alternatives
    }

    public Control validate(){
        return evaluate(validateStream());
    }

    protected abstract Control evaluate(Stream<Control> s);

    protected abstract Function<Method, Boolean> sortPredicate();

    protected abstract Function<Method, Control> validateAlgorithm();

    @FunctionalInterface
    protected interface ThrowingFunction<I, R, E extends Exception> {
        R accept(I s) throws E;
    }

    protected <I,R> Function<I, R> invokeAndHandle(ThrowingFunction<I, R, Exception> throwingFunction, Function<I, R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                return fallback.apply(i);
            } catch (InvalidPropertiesFormatException e) {
                throw new InvalidFieldException(e.getMessage());
            } catch (ConflictFieldException e) {
                throw new ConflictFieldException(e.getMessage());
            } catch (RequirementsException e) {
                throw new RequirementsException(e.getMessage());
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    protected <I,R>Function<I, R> invokeAndHandle(ThrowingFunction<I, R, Exception> throwingFunction, Supplier<R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                return fallback.get();
            } catch (InvalidPropertiesFormatException e) {
                throw new InvalidFieldException(e.getMessage());
            } catch (RequirementsException e) {
                throw new RequirementsException(e.getMessage());
            } catch (IllegalArgumentException e) {
                throw new AnnotationException(e.getMessage() + ". Have you annotated your field correctly?");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
    }


    /**
     * @param throwingFunction a function which takes a string and return a method
     * @return the method of the given class
     * @throws RuntimeException if method is not found or other exceptions are catch
     */
    protected Function<? super String, Method> fetchMethod(ThrowingFunction<String, Method, NoSuchMethodException> throwingFunction) throws RuntimeException{
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
}