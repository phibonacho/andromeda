package com.annotation.validate.types;

import java.util.InvalidPropertiesFormatException;

public abstract class AbstractNumericType extends AbstractValidateType<Number> {

    @Override
    public Boolean check(Number guard) throws InvalidPropertiesFormatException {
        if(guard == null) throw new InvalidPropertiesFormatException("null argument");
        return true;
    }
}
