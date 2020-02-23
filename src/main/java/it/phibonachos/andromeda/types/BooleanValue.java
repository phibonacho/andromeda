package it.phibonachos.andromeda.types;

import java.util.InvalidPropertiesFormatException;

public class BooleanValue extends AbstractValidateType<Boolean> {
    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Boolean)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Boolean");
        return true;
    }

    @Override
    public Boolean check(Boolean guard) throws InvalidPropertiesFormatException {
        return !guard ? null : true;
    }
}
