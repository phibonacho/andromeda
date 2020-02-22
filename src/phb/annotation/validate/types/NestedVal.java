package phb.annotation.validate.types;

import phb.annotation.validate.ValidateEvaluator;
import phb.annotation.validate.exception.InvalidFieldException;
import phb.annotation.validate.exception.InvalidNestedFieldException;

public class NestedVal<T> extends AbstractValidateType<T> {
    @Override
    public Boolean isInstance(Object obj) {
        return true;
    }

    @Override
    public Boolean check(T guard) throws InvalidFieldException, InvalidNestedFieldException {
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