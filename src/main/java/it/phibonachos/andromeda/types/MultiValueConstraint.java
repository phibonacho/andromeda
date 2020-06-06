package it.phibonachos.andromeda.types;

import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.utils.FunctionalWrapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Defines a {@link Constraint} which elaborate boolean verdicts, it can be used to validate a single or multiple properties.
 */
public abstract class MultiValueConstraint implements Constraint<Boolean> {
    protected String[] context, ignoreContext;

    /**
     * Evaluate method collect all the properties needed by the validation class and retrieves them values in order to emit a verdict.
     * @param target Object from which props will be invoked
     * @param props properties bounded to validation class (1-n)
     * @param <Target> Type of target param
     * @return true if all clauses are met.
     * @throws Exception if at least one clause is not met or validate algorithm emit a negative verdict.
     */
    public <Target> Boolean evaluate(Target target, Method... props) throws Exception {
        Supplier<Stream<Object>> sup = () -> Arrays.stream(props).map(FunctionalWrapper.tryCatch(m -> m.invoke(target), m -> null));
        if(sup.get().anyMatch(Objects::isNull))
            throw new NullPointerException();

        if(!validateAll(sup.get().toArray()))
            throw new InvalidFieldException(" " + message());
        return true;
    }

    /**
     * ValidateAll wraps the validation algorithm, in {@link SingleValueConstraint} and {@link CoupleConstraint} this method is masked with arity fixed method.
     * @param objects properties bundled to validation.
     * @return true if validation algorithm emit a positive verdict.
     * @throws Exception if validation algorithm emit a negative verdict.
     */
    protected abstract Boolean validateAll(Object ...objects) throws Exception;

    /**
     * @return the string specifying why the validation failed.
     */
    public String message() {
        return "fails constraint defined in " + this.getClass().getSimpleName();
    }

    public void setContext(Set<String> context) {
        this.context = context.toArray(new String[0]);
    }

    public void setIgnoreContext(Set<String> ignoreContext) {
        this.ignoreContext = ignoreContext.toArray(new String[0]);
    }
}
