package it.phibonachos.andromeda.types.mono;

import it.phibonachos.andromeda.types.SingleValueConstraint;
import org.apache.commons.lang3.StringUtils;

public class StringValue extends SingleValueConstraint<String> {

    @Override
    public Boolean validate(String guard) {
        return !StringUtils.isBlank(guard);
    }
}