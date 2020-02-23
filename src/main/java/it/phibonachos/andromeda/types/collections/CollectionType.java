package it.phibonachos.andromeda.types.collections;
import it.phibonachos.andromeda.exception.InvalidCollectionFieldException;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.andromeda.types.AbstractValidateType;

import java.util.Collection;

public class CollectionType<T, C extends Collection<T>> extends AbstractValidateType<C> {

    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Collection)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Collection");
        return true;
    }

    @Override
    public Boolean check(C guard) throws InvalidFieldException {
        if(guard == null)
            throw new InvalidCollectionFieldException(" cannot be null.");

        return true;
    }

}
