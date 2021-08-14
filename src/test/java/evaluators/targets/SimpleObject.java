package evaluators.targets;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class SimpleObject {
    private java.lang.String prop;
    private java.lang.String prop2;

    @Validate(with = StringConstraint.class, mandatory = true, context = "ctx1")
    public java.lang.String getProp() {
        return prop;
    }

    public void setProp(java.lang.String prop) {
        this.prop = prop;
    }

    @Validate(with = StringConstraint.class, mandatory = true, context = "ctx2")
    public java.lang.String getProp2() {
        return prop2;
    }

    public void setProp2(java.lang.String prop2) {
        this.prop2 = prop2;
    }
}
