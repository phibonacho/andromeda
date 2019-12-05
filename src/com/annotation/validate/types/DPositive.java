package com.annotation.validate.types;

import java.util.InvalidPropertiesFormatException;

public class DPositive extends DoubleValue {
    @Override
    public Boolean check(Double guard) throws InvalidPropertiesFormatException {
        if(!super.check(guard) || guard == 0) throw new NullPointerException();
        return true;
    }
}
