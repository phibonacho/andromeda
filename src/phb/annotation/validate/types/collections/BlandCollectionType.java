package phb.annotation.validate.types.collections;
import phb.annotation.validate.exception.InvalidCollectionFieldException;
import phb.annotation.validate.exception.InvalidFieldException;

import java.util.Collection;

public class BlandCollectionType<T, C extends Collection<T>> extends CollectionType<T, C> {

    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Collection)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Collection");
        return true;
    }

    @Override
    public Boolean check(C guard) throws InvalidFieldException {
        if(super.check(guard) && guard.size() == 0)
            throw new InvalidCollectionFieldException(" is empty.");

        return true;
    }

}
