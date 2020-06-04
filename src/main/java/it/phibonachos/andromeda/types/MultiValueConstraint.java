package it.phibonachos.andromeda.types;

import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.utils.FunctionalWrapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class MultiValueConstraint implements Constraint<Boolean> {
    protected String[] context, ignoreContext;

    public <Target> Boolean evaluate(Target target, Method... props) throws Exception {
        Supplier<Stream<Object>> sup = () -> Arrays.stream(props).map(FunctionalWrapper.tryCatch(m -> m.invoke(target), m -> null));
        if(sup.get().anyMatch(Objects::isNull))
            throw new NullPointerException();

        if(!validateAll(sup.get().toArray()))
            throw new InvalidFieldException(" " + message());
        return true;
    }

    public abstract Boolean validateAll(Object ...objects) throws Exception;

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
