package it.phibonachos.andromeda.types;

import java.util.InvalidPropertiesFormatException;

public abstract class AbstractNumericType<T> extends AbstractValidateType<T> {

    @Override
    public Boolean check(T guard) throws InvalidPropertiesFormatException {
        if(guard == null) throw new InvalidPropertiesFormatException("null argument");
        return true;
    }
}
