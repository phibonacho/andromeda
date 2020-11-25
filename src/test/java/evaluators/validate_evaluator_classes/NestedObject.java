package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.NestedVal;
import it.phibonachos.andromeda.types.mono.StringValue;

public class NestedObject {
    private String prop;
    private SimpleObject so;

    @Validate(with = StringValue.class, mandatory = true)
    public String getProp() {
        return prop;
    }



    public void setProp(String prop) {
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
