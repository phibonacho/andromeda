package com.annotation.validate.types;

import java.util.InvalidPropertiesFormatException;

public class LPositive extends LongValue {
    @Override
    public Boolean check(Number guard) throws InvalidPropertiesFormatException {
        if(!super.check(guard) || guard.doubleValue() == 0) throw new NullPointerException();
        return true;
    }
}
