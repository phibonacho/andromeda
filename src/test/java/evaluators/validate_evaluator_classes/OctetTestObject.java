package evaluators.validate_evaluator_classes;

import evaluators.constraints.C8Constraint;
import it.phibonachos.andromeda.Validate;

public class OctetTestObject {
    private String prop1;
    private String prop2;
    private String prop3;
    private String prop4;
    private String prop5;
    private String prop6;
    private String prop7;
    private String prop8;


    @Validate(with = C8Constraint.class, mandatory = true, boundTo = {"prop2", "prop3", "prop4", "prop5", "prop6", "prop7", "prop8"})
    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }

    public String getProp3() {
        return prop3;
    }

    public void setProp3(String prop3) {
        this.prop3 = prop3;
    }

    public String getProp4() {
        return prop4;
    }

    public void setProp4(String prop4) {
        this.prop4 = prop4;
    }

    public String getProp5() {
        return prop5;
    }

    public void setProp5(String prop5) {
        this.prop5 = prop5;
    }

    public String getProp6() {
        return prop6;
    }

    public void setProp6(String prop6) {
        this.prop6 = prop6;
    }

    public String getProp7() {
        return prop7;
    }

    public void setProp7(String prop7) {
        this.prop7 = prop7;
    }

    public String getProp8() {
        return prop8;
    }

    public void setProp8(String prop8) {
        this.prop8 = prop8;
    }
}
