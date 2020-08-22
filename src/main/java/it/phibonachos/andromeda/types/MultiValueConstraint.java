package it.phibonachos.andromeda.types;

import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.ponos.converters.MultiValueConverter;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Defines a {@link Constraint} which elaborate boolean verdicts, it can be used to validate a single or multiple properties.
 */
public abstract class MultiValueConstraint extends MultiValueConverter<Boolean> implements Constraint {
    protected String[] context, ignoreContext;

    @Override
    public <Target> Boolean evaluate(Target target, Method... props) throws Exception {
        if(!super.evaluate(target, props))
            throw new InvalidFieldException(" " + message());
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
