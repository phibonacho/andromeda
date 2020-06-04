package it.phibonachos.andromeda.types;

import java.util.Set;

public abstract class RegexClass extends StringValue {

    public abstract String regex();

    @Override
    public Boolean validate(String guard) {
        return super.validate(guard) && guard.matches(regex());
    }

    @Override
    public String message() {
        return "Do not match regex: " + regex();
    }
}
