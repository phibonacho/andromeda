package evaluators.validate_evaluator_classes;

import evaluators.constraints.C2Constraint;
import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringValue;

public class BoundReouirement {
    private String prop;
    private String boundProp;
    private String propWithRequirements;

    @Validate(with = StringValue.class, mandatory = true)
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = C2Constraint.class, mandatory = true, boundTo = "prop")
    public String getBoundProp() {
        return boundProp;
    }

    public void setBoundProp(String boundProp) {
        this.boundProp = boundProp;
    }

    @Validate(with = StringValue.class, mandatory = true, requires = "boundProp")
    public String getPropWithRequirements() {
        return propWithRequirements;
    }

    public void setPropWithRequirements(String propWithRequirements) {
        this.propWithRequirements = propWithRequirements;
    }
}
