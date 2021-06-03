package it.phibonachos.andromeda.types.mono;

import it.phibonachos.andromeda.types.SoloConstraint;
import org.apache.commons.lang3.StringUtils;

public class StringConstraint extends SoloConstraint<java.lang.String> {

    @Override
    public Boolean validate(java.lang.String guard) {
        return !StringUtils.isBlank(guard);
    }
}