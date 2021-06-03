package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.NestedVal;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class NestedObject {
    private java.lang.String prop;
    private SimpleObject so;

    @Validate(with = StringConstraint.class, mandatory = true)
    public java.lang.String getProp() {
        return prop;
    }



    public void setProp(java.lang.String prop) {
        this.prop = prop;
    }

    @Validate(with = NestedVal.class)
    public SimpleObject getSo() {
        return so;
    }

    public void setSo(SimpleObject so) {
        this.so = so;
    }
}
