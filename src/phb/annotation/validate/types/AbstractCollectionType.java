package phb.annotation.validate.types;
import phb.annotation.validate.ValidateEvaluator;
import phb.annotation.validate.exception.InvalidCollectionFieldException;
import phb.annotation.validate.exception.InvalidFieldException;
import phb.utils.FunctionalUtils;

import java.util.Collection;

public class AbstractCollectionType<T, C extends Collection<T>> extends AbstractValidateType<C> {

    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Collection)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Collection");
        return true;
    }

    @Override
    public Boolean check(C guard) throws InvalidFieldException {
        if(guard == null)
            throw new InvalidCollectionFieldException(" cannot be null.");

        if(guard.size() == 0)
            throw new InvalidCollectionFieldException(" is empty.");

        return guard.stream()
                .map(FunctionalUtils.tryCatch(item -> new ValidateEvaluator<>(item).validate()))
                .reduce((a, b) -> a && b)
                .orElse(false);
    }

}
