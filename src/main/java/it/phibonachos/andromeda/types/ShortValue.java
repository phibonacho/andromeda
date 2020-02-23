package it.phibonachos.andromeda.types;

public class ShortValue extends AbstractNumericType {
    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Short)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Short");
        return true;
    }
}
