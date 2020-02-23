package phb.utils;

import phb.annotation.validate.exception.InvalidCollectionFieldException;
import phb.annotation.validate.exception.InvalidFieldException;
import phb.annotation.validate.exception.InvalidNestedFieldException;

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
