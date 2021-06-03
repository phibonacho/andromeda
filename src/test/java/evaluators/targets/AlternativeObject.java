package evaluators.targets;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class AlternativeObject {
    private java.lang.String prop;
    private java.lang.String alternativeProp;

    @Validate(with = StringConstraint.class, mandatory = true, alternatives = "alternativeProp", context = "ctx1")
    public java.lang.String getProp() {
        return prop;
    }

    public void setProp(java.lang.String prop) {
        this.prop = prop;
    }

    @Validate(with = StringConstraint.class)
    public java.lang.String getAlternativeProp() {
        return alternativeProp;
    }

    public void setAlternativeProp(java.lang.String alternativeProp) {
        this.alternativeProp = alternativeProp;
    }
}
