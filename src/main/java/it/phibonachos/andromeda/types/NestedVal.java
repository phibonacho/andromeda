package it.phibonachos.andromeda.types;

import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.andromeda.exception.InvalidNestedFieldException;

public class NestedVal<T> extends SingleValueConstraint<T> {

    @Override
    public Boolean validate(T guard) throws InvalidFieldException, InvalidNestedFieldException {
        try {
            return new ValidateEvaluator<>(guard).validate();
        } catch (Exception e) {
            if(e instanceof InvalidFieldException)
                throw new InvalidNestedFieldException("." + e.getMessage());
            else if(e instanceof NullPointerException)
                throw new InvalidNestedFieldException(" cannot be null.");
            throw new InvalidNestedFieldException(e.getMessage());
        }
    }
}