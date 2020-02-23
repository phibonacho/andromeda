package it.phibonachos.utils;

import it.phibonachos.andromeda.exception.InvalidCollectionFieldException;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.andromeda.exception.InvalidNestedFieldException;

import java.util.function.Function;

public class FunctionalUtils {
    @FunctionalInterface
    public interface ThrowingFunction<I, R, E extends Exception> {
        R accept(I s) throws E;
    }

    public static <I,R> Function<I, R> tryCatch(ThrowingFunction<I, R, Exception> throwingFunction) throws InvalidFieldException {
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
}
