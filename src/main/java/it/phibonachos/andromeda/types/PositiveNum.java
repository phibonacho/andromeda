package it.phibonachos.andromeda.types;

import java.util.Optional;

public class PositiveNum<NT extends Number> extends NumericConstraint<NT> {
    @Override
    public Boolean validate(NT number) {
        return super.validate(number) && Optional.ofNullable(number.doubleValue() > 0 ? true : null).orElseThrow(NullPointerException::new);
    }
}
