package it.phibonachos.andromeda.types.mono;

import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.andromeda.exception.InvalidNestedFieldException;
import it.phibonachos.andromeda.types.SingleValueConstraint;
import it.phibonachos.ponos.converters.ConverterException;

/**
 * <p>This class provides a handful way to propagate validation on nested objects.
 * A new validator will be instantiated to validate the child object.</p>
 *
 * @param <T> Generic class to be validated
 */
public class NestedVal<T> extends SingleValueConstraint<T> {

    @Override
    public Boolean validate(T guard) throws Exception {
        return new ValidateEvaluator<>(guard)
                .onlyContexts(this.context)
                .ignoreContexts(this.ignoreContext)
                .validate();
    }
}