package it.phibonachos.andromeda.types.collections;
import it.phibonachos.andromeda.exception.InvalidCollectionFieldException;
import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.utils.FunctionalUtils;

import java.util.Collection;

public class StrongCollectionType<T, C extends Collection<T>> extends BlandCollectionType<T, C> {

    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Collection)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Collection");
        return true;
    }

    @Override
    public Boolean check(C guard) throws InvalidFieldException {
        if(super.check(guard) && guard.size() == 0)
            throw new InvalidCollectionFieldException(" is empty.");

        return guard.stream()
                .map(FunctionalUtils.tryCatch(item -> new ValidateEvaluator<>(item).validate()))
                .reduce((a, b) -> a && b)
                .orElse(false);

    }

}
