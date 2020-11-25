package evaluators;


import evaluators.validate_evaluator_classes.ComplexObject;
import it.phibonachos.andromeda.ValidateEvaluator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SortTest {

    @Test
    public void simpleConstraintFirst() throws Exception {
        // TODO
        ComplexObject test = new ComplexObject();
        ValidateEvaluator<ComplexObject> validator = new ValidateEvaluator<>(test);

        // simple validation should be validated as first
        try {
            assert validator.validate();
        } catch (Exception e) {
            assert e.getMessage().equals("prop1 cannot be null");
        }

        test.setProp1("prop1");

        // validation with requirements are validated after all simple validations
        try {
            assert validator.validate();
        } catch (Exception e) {
            assert e.getMessage().equals("prop2 cannot be null");
        }

        test.setProp2("prop2");

        // validation with bounded values are validated after all with requirements
        try {
            assert validator.validate();
        } catch (Exception e) {
            assert e.getMessage().equals("prop3 cannot be null");
        }

        test.setProp3(true);

        // validations with bounds and requirements are validated at last
        try {
            assert validator.validate();
        } catch (Exception e) {
            assert e.getMessage().equals("prop4 cannot be null");
        }

        test.setProp4("prop4");

        try {
            assert validator.validate();
        } catch (Exception e) {
            assert false;
        }
    }
}