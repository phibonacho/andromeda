package phb.annotation.validate.types;

import phb.annotation.validate.exception.InvalidFieldException;

public abstract class AbstractValidateType<GuardType> implements ValidateTypeInterface<Boolean, GuardType> {

    protected AbstractValidateType() {}

    public Boolean validate(GuardType guard) throws Exception {
        return isInstance(guard) && check(guard);
    }

    public abstract Boolean isInstance(Object obj);

    public abstract Boolean check(GuardType guard) throws Exception;
}
