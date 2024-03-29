package it.phibonachos.utils;

import it.phibonachos.andromeda.exception.InvalidCollectionFieldException;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.andromeda.exception.InvalidNestedFieldException;

import java.util.function.Function;
import java.util.function.Supplier;

public class FunctionalUtils {
    public static <I,R> Function<I, R> tryCatch(FunctionalWrapper<I, R, Exception> throwingFunction) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (InvalidFieldException | InvalidNestedFieldException e) {
                throw new InvalidCollectionFieldException(e.getMessage());
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <I,R> Function<I, R> tryCatch(FunctionalWrapper<I, R, Exception> throwingFunction, Function<I,R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (Exception e) {
                return fallback.apply(i);
            }
        };
    }

    public static <I,R> Function<I, R> tryCatch(FunctionalWrapper<I, R, Exception> throwingFunction, Supplier<R> fallback) throws InvalidFieldException {
        return i -> {
            try {
                return throwingFunction.accept(i);
            } catch (Exception e) {
                return fallback.get();
            }
        };
    }

}
