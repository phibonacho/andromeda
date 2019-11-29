package com.annotation.validate.types;

import com.annotation.validate.types.ValidateTypeInterface;

import java.util.InvalidPropertiesFormatException;

public abstract class AbstractValidateType<GuardType> implements ValidateTypeInterface<Boolean, GuardType> {

    protected AbstractValidateType() {}

    public Boolean validate(GuardType guard) throws IllegalArgumentException, InvalidPropertiesFormatException {
        return isInstance(guard) && check(guard);
    }

    public abstract Boolean isInstance(Object obj);

    public abstract Boolean check(GuardType guard) throws InvalidPropertiesFormatException;
}
