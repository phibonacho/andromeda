package phb.annotation.validate.types.collections;
import phb.annotation.validate.exception.InvalidCollectionFieldException;
import phb.annotation.validate.exception.InvalidFieldException;
import phb.annotation.validate.types.AbstractValidateType;

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
