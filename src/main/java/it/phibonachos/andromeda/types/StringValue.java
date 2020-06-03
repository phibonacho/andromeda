package it.phibonachos.andromeda.types;

import org.apache.commons.lang3.StringUtils;

public class StringValue extends SingleValueConstraint<String> {
    @Override
    public Boolean validate(String guard) {
        return !StringUtils.isBlank(guard);
    }
}