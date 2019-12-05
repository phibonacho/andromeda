package com.annotation.validate.types;
import com.annotation.validate.ValidateEvaluator;
import com.annotation.validate.exception.InvalidCollectionFieldException;
import com.annotation.validate.exception.InvalidFieldException;
import com.annotation.validate.exception.InvalidNestedFieldException;

import java.util.Collection;
import java.util.function.Function;

public class AbstractCollectionType<T, C extends Collection<T>> extends AbstractValidateType<C> {

    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Collection)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Collection");
        return true;
    }

    @Override
    public Boolean check(C guard) throws InvalidFieldException {
        if(guard == null)
            throw new InvalidCollectionFieldException(" cannot be null.");

        if(guard.size() == 0)
            throw new InvalidCollectionFieldException(" is empty.");
        return guard.stream()
                .map(tryCatch(item -> new ValidateEvaluator<>(item).evaluate()))
                .reduce((a, b) -> a && b)
                .orElse(false);
    }

    @FunctionalInterface
    protected interface ThrowingFunction<I, R, E extends Exception> {
        R accept(I s) throws E;
    }

    protected <I,R> Function<I, R> tryCatch(ThrowingFunction<I, R, Exception> throwingFunction) throws InvalidFieldException {
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
