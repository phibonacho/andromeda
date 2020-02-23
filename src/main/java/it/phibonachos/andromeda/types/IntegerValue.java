package it.phibonachos.andromeda.types;

public class IntegerValue extends AbstractNumericType<Integer> {
    @Override
    public Boolean isInstance(Object obj) {
        if(!(obj instanceof Double)) throw new IllegalArgumentException(obj.getClass().getName() + " do not match Double");
        return true;
    }
}
