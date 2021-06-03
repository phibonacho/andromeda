package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class CascadeAlternativeObject {
    private java.lang.String prop;
    private java.lang.String aProp;
    private java.lang.String aProp1;


    @Validate(with = StringConstraint.class, mandatory = true, alternatives = "aProp")
    public java.lang.String getProp() {
        return prop;
    }

    public void setProp(java.lang.String prop) {
        this.prop = prop;
    }

    @Validate(with = StringConstraint.class, alternatives = "aProp1")
    public java.lang.String getAProp() {
        return aProp;
    }

    public void setAProp(java.lang.String alternativeProp) {
        this.aProp = alternativeProp;
    }

    public java.lang.String getAProp1() {
        return aProp1;
    }

    public void setAProp1(java.lang.String aProp1) {
        this.aProp1 = aProp1;
    }
}
