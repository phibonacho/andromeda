package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringValue;

public class ConflictsObject {
    private String prop;
    private String conflictProp;

    @Validate(with = StringValue.class, conflicts = "conflictProp")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class, conflicts = "prop")
    public String getConflictProp() {
        return conflictProp;
    }

    public void setConflictProp(String conflictProp) {
        this.conflictProp = conflictProp;
    }
}
