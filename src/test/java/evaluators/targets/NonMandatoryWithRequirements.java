package evaluators.targets;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class NonMandatoryWithRequirements {
    private java.lang.String prop;
    private java.lang.String requireProp;

    @Validate(with = StringConstraint.class, requires = "requireProp")
    public java.lang.String getProp() {
        return prop;
    }

    public void setProp(java.lang.String prop) {
        this.prop = prop;
    }

    @Validate(with = StringConstraint.class)
    public java.lang.String getRequireProp() {
        return requireProp;
    }

    public void setRequireProp(java.lang.String requireProp) {
        this.requireProp = requireProp;
    }
}
