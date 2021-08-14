package evaluators.targets;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.StringConstraint;

public class ConflictsObject {
    private java.lang.String prop;
    private java.lang.String conflictProp;

    @Validate(with = StringConstraint.class, conflicts = "conflictProp")
    public java.lang.String getProp() {
        return prop;
    }

    public void setProp(java.lang.String prop) {
        this.prop = prop;
    }

    @Validate(with = StringConstraint.class, conflicts = "prop")
    public java.lang.String getConflictProp() {
        return conflictProp;
    }

    public void setConflictProp(java.lang.String conflictProp) {
        this.conflictProp = conflictProp;
    }
}
