package it.phibonachos.andromeda.types.mono;

import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.andromeda.exception.InvalidNestedFieldException;
import it.phibonachos.andromeda.types.SingleValueConstraint;

/**
 * <p>This class provides a handful way to propagate validation on nested objects.
 * A new validator will be instantiated to validate the child object.</p>
 *
 * @param <T> Generic class to be validated
 */
public class NestedVal<T> extends SingleValueConstraint<T> {

    @Override
    public Boolean validate(T guard) throws InvalidFieldException, InvalidNestedFieldException, NullPointerException {
        try {
            return new ValidateEvaluator<>(guard)
                    .onlyContexts(this.context)
                    .ignoreContexts(this.ignoreContext)
                    .validate();
        } catch (Exception e) {
            if(e instanceof InvalidFieldException)
                throw new InvalidNestedFieldException("." + e.getMessage());
            else if(e instanceof NullPointerException)
                throw new InvalidNestedFieldException(" cannot be null.");
            throw new InvalidNestedFieldException(e.getMessage());
        }
    }
}