package it.phibonachos.evaluators;

import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.utils.FunctionalWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
                .filter(this::customFilter)
                .sorted(Comparator.comparing(sortPredicate())) // mandatory fields as first
                .map(validateAlgorithm());
    }

    public Control validate(){
        return evaluate(validateStream());
    }

    protected abstract Control evaluate(Stream<Control> s);

    protected abstract Function<Method, Boolean> sortPredicate();

    protected abstract Function<Method, Control> validateAlgorithm();

    protected Boolean customFilter(Method m) {
        return true;
    }

    /**
     * Invoke method against a class and uses fallback in case of nullpointer
     * @param throwingFunction wraps method invocation
     * @param fallback invoked when throwingFunction results in nullpointer
     * @param <R> parametric return type
     * @return result of the invocation in throwingFunction or value provided from fallback function
     * @throws InvalidFieldException evaluation non-null but invalid
     */
    protected abstract  <R> Function<Method, R> invokeOnNull(FunctionalWrapper<Method, R, Exception> throwingFunction, Function<Method, R> fallback) throws InvalidFieldException;

    /**
     * Same as {@link #invokeOnNull(FunctionalWrapper, Function)} but takes a supplier instead of a function (no params needed)
     * */
    protected abstract  <R>Function<Method, R> invokeOnNull(FunctionalWrapper<Method, R, Exception> throwingFunction, Supplier<R> fallback) throws InvalidFieldException;


    /**
     * Return getter method starting from property name
     * @param throwingFunction algorithm to retrieve a getMethod using its name
     * @return the method of the given class
     * @throws RuntimeException if method is not found or other exceptions are catch
     */
    @Deprecated
    protected Function<? super String, Method> fetchMethod(FunctionalWrapper<String, Method, NoSuchMethodException> throwingFunction) throws RuntimeException{
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

    protected A getMainAnnotation(Method m) {
        return m.getAnnotation(annotationClass);
    }
}