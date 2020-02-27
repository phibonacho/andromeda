package it.phibonachos.andromeda.types;

public class PositiveNum<NT extends Number> extends NumericConstraint<NT> {
    @Override
    public Boolean validate(NT number) {
        return super.validate(number) && number.intValue() > 0;
    }
}
