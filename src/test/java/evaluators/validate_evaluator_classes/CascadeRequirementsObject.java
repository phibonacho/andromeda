package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class CascadeRequirementsObject {
    private java.lang.String prop;
    private java.lang.String req1;
    private java.lang.String req2;
    private java.lang.String req3;

    @Validate(with = StringConstraint.class, requires = "req1", mandatory = true, context = "ctx1")
    public java.lang.String getProp() {
        return prop;
    }

    public void setProp(java.lang.String prop) {
        this.prop = prop;
    }

    @Validate(with = StringConstraint.class, requires = "req2", mandatory = true, context = "ctx2")
    public java.lang.String getReq1() {
        return req1;
    }

    public void setReq1(java.lang.String req1) {
        this.req1 = req1;
    }

    @Validate(with = StringConstraint.class, requires = "req3")
    public java.lang.String getReq2() {
        return req2;
    }

    public void setReq2(java.lang.String req2) {
        this.req2 = req2;
    }

    @Validate(with = StringConstraint.class)
    public java.lang.String getReq3() {
        return req3;
    }

    public void setReq3(java.lang.String req3) {
        this.req3 = req3;
    }
}
