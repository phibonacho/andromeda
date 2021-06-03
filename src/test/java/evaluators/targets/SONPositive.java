package evaluators.targets;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.mono.PositiveNum;

public class SONPositive {
    private int prop;

    @Validate(with = PositiveNum.class, mandatory = true)
    public int getProp() {
        return prop;
    }

    public void setProp(int prop) {
        this.prop = prop;
    }
}
