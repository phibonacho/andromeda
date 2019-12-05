package com.evaluators;

import com.annotation.validate.exception.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InvalidPropertiesFormatException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractEvaluator<Target, Control, A extends Annotation> {
    protected Target t;
    protected Class<A> annotationClass;

    public AbstractEvaluator(Target t){
        this.t = t;
    }

    private Stream<Control> validateStream() {
        return Arrays.stream(this.t.getClass().getDeclaredMethods())
                .filter(m -> m.getAnnotation(annotationClass)!=null)
                .filter(m -> m.getParameterCount() == 0)
                .sorted(Comparator.comparing(sortPredicate())) // mandatory fields as first
                .map(validateAlgorithm());
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

    /**
     * @param throwingFunction a function capable of throwing exceptions
     * @param fallback a function to call in case of failure
     * @param <R> a return type
     * @return the result of the evaluation of throwing function or fallback
     * @throws InvalidFieldException if evaluation non-null but invalid
     */
    protected <R> Function<Method, R> invokeOnNull(ThrowingFunction<Method, R, Exception> throwingFunction, Function<Method, R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                return fallback.apply(i);
            } catch (InvalidPropertiesFormatException | InvalidFieldException e) {
                throw new InvalidFieldException(e.getMessage());
            } catch (InvalidNestedFieldException e) {
                throw new InvalidFieldException(displayName(i.getName()) + e.getMessage());
            } catch (InvalidCollectionFieldException e) {
                throw new InvalidFieldException("Collection " + displayName(i.getName()) + "[] : " + e.getMessage());
            } catch (ConflictFieldException e) {
                throw new ConflictFieldException(e.getMessage());
            } catch (RequirementsException e) {
                throw new RequirementsException(e.getMessage());
            } catch (CyclicRequirementException e) {
                throw new CyclicRequirementException(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Same as {@link #invokeOnNull(ThrowingFunction, Function)} but takes a supplier instead of a function (no params needed)
     * */
    protected <R>Function<Method, R> invokeOnNull(ThrowingFunction<Method, R, Exception> throwingFunction, Supplier<R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (NullPointerException npe) {
                return fallback.get();
            } catch (InvalidPropertiesFormatException | InvalidFieldException e) {
                throw new InvalidFieldException(e.getMessage());
            } catch (InvalidNestedFieldException e) {
                throw new InvalidFieldException(displayName(i.getName()) + e.getMessage());
            } catch (InvalidCollectionFieldException e) {
                throw new InvalidFieldException("Collection " + displayName(i.getName()) + "[] : " + e.getMessage());
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

    private String displayName(String method){
        return Stream.of(method).map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).collect(Collectors.joining());
    }

}