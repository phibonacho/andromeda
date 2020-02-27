package it.phibonachos.andromeda.types.collections;
import it.phibonachos.andromeda.exception.InvalidCollectionFieldException;
import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.utils.FunctionalUtils;

import java.util.Collection;

public class StrictCollectionType<T, C extends Collection<T>> extends BlandCollectionType<T, C> {

    @Override
    public Boolean validate(C guard) throws InvalidFieldException {
        return super.validate(guard) && guard.size() > 0 && guard.stream()
            .map(FunctionalUtils.tryCatch(item -> new ValidateEvaluator<>(item).validate()))
            .reduce((a, b) -> a && b)
            .orElse(false);

    }

}
