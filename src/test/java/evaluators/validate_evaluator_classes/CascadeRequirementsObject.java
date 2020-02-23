package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.StringValue;

public class CascadeRequirementsObject {
    private String prop;
    private String req1;
    private String req2;
    private String req3;

    @Validate(with = StringValue.class, require = "req1", mandatory = true)
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class, require = "req2")
    public String getReq1() {
        return req1;
    }

    public void setReq1(String req1) {
        this.req1 = req1;
    }

    @Validate(with = StringValue.class, require = "req3")
    public String getReq2() {
        return req2;
    }

    public void setReq2(String req2) {
        this.req2 = req2;
    }

    @Validate(with = StringValue.class)
    public String getReq3() {
        return req3;
    }

    public void setReq3(String req3) {
        this.req3 = req3;
    }
}
