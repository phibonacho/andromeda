package it.phibonachos.andromeda.types;

import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.ponos.converters.MultiValueConverter;

import java.util.Objects;
import java.util.Set;

/**
 * Defines a {@link Constraint} which elaborate boolean verdicts, it can be used to validate a single or multiple properties.
 */
public abstract class MultiConstraint extends MultiValueConverter<Boolean> implements Constraint {
    protected String[] context, ignoreContext;

    @Override
    public Boolean evaluate(Object... props) throws Exception {
        if(Objects.isNull(props[0]))
            throw new NullPointerException();

        if(!super.evaluate(props))
            throw new InvalidFieldException(this.message());

        return true;
    }

    @Override
    public String message() {
        return "fails constraint defined in " + this.getClass().getSimpleName();
    }

    @Override
    public void setContext(Set<String> context) {
        this.context = context.toArray(new String[0]);
    }

    @Override
    public void setIgnoreContext(Set<String> ignoreContext) {
        this.ignoreContext = ignoreContext.toArray(new String[0]);
    }
}
