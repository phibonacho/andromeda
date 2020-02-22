package phb.annotation.validate.types;

public class LongValue extends AbstractNumericType<Long> {
    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Long)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Long");
        return true;
    }
}
