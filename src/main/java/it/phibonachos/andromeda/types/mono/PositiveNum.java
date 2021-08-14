package it.phibonachos.andromeda.types.mono;

import java.util.Optional;

public class PositiveNum<NT extends Number> extends NumericConstraint<NT> {
    @Override
    public Boolean validate(NT number) {
        return super.validate(number) && Optional.ofNullable(number.doubleValue() > 0 ? true : null).orElse(false);
    }

    @Override
    public String message() {
        return "0 value detected";
    }
}
