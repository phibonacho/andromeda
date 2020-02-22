package phb.annotation.validate.types;

public class DoubleValue extends AbstractNumericType<Double> {
    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Double)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Double");
        return true;
    }
}
