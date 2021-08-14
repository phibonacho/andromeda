package evaluators.validate.clauses;


import evaluators.targets.*;
import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BoundToClauseTest {

    /* POSITIVE TEST */

    @Test
    public void successfulCompoundConstraint() {
        CompoundConstraintObject cco = new CompoundConstraintObject();
        ValidateEvaluator<CompoundConstraintObject> ve = new ValidateEvaluator<>(cco);
        cco.setProp("this is mandatory");
        cco.setProp1(true);
        cco.setProp2("this is required since prop1 is true");

        try {
            assert ve.validate();
        } catch (Exception e) {
            assert false;
        }

        try {
            cco.setProp1(false);
            ve.validate();
        } catch (Exception ife) {
            assert ife instanceof InvalidFieldException;
            assert ife.getMessage().contains("incompatible values");
        }

        cco.setProp2(null);

        try {
            assert ve.validate();
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    public void C8ConstraintTest() {
        OctetTestObject oto = new OctetTestObject();

        oto.setProp1("ciao");

        try {
            new ValidateEvaluator<>(oto).validate();
        } catch (Exception e) {
            assert  false;
        }
    }

    /* NEGATIVE TEST */

}