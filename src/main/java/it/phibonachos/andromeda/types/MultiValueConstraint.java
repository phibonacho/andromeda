package it.phibonachos.andromeda.types;

import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.utils.FunctionalUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class MultiValueConstraint implements Constraint<Boolean> {

    public <Target> Boolean evaluate(Target target, Method... props) throws Exception {
        Supplier<Stream<Object>> sup = () -> Arrays.stream(props).map(FunctionalUtils.tryCatch(m -> m.invoke(target), m -> null));
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
}
