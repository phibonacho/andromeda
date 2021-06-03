package it.phibonachos.andromeda.types.mono;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.andromeda.exception.InvalidNestedFieldException;
import it.phibonachos.andromeda.types.SoloConstraint;

import java.util.Optional;

/**
 * <p>This class provides the simplest validation possible and is the default validation class for {@link Validate#with()} clause.</p>
 * @param <T> A generic class to be validated.
 */
public class NotNull<T> extends SoloConstraint<T> {

    @Override
    public Boolean validate(T guard) throws InvalidFieldException, InvalidNestedFieldException, NullPointerException {
        return Optional.ofNullable(guard)
                .map(t -> true)
                .orElseThrow(NullPointerException::new);
    }
}