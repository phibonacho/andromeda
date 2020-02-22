package phb.annotation.validate.types;

import java.util.InvalidPropertiesFormatException;

public class LPositive extends LongValue {
    @Override
    public Boolean check(Long guard) throws InvalidPropertiesFormatException {
        if(!super.check(guard) || guard == 0) throw new NullPointerException();
        return true;
    }
}
