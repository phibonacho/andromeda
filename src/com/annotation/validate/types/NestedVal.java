package com.annotation.validate.types;

import com.annotation.validate.ValidateEvaluator;

public class NestedVal<T> extends AbstractValidateType<T> {
    @Override
    public Boolean isInstance(Object obj) {
        return true;
    }

    @Override
    public Boolean check(T guard) throws Exception {
        if(!new ValidateEvaluator<>(guard).evaluate())
            throw new NullPointerException();
        return true;
    }
}
