package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringValue;

public class SimpleObject {
    private String prop;
    private String prop2;

    @Validate(with = StringValue.class, mandatory = true, context = "ctx1")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class, mandatory = true, context = "ctx2")
    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }
}
