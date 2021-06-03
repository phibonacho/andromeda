package evaluators;


import evaluators.targets.*;
import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class ValidateEvaluatorTest {
    /* OUT OF THE BOX VALIDATION CLASSES */

    @Test
    public void listCollectionTest() throws Exception {
        CollectionObject co = new CollectionObject();
        List<SimpleObject> l = new ArrayList<>();
        co.setMyPrivateList(l);
        ValidateEvaluator<CollectionObject> ve = new ValidateEvaluator<>(co);
        assert ve.validate();
    }

    @Test
    public void nestedValidation() {
        NestedObject no = new NestedObject();
        ValidateEvaluator<NestedObject> ve = new ValidateEvaluator<>(no).onlyContexts("ctx1");
        no.setProp("this is mandatory");

        try {
            assert ve.validate();
        } catch (Exception e) {
            assert false;
        }
        SimpleObject so = new SimpleObject();
        no.setSo(so);
        try {
            ve.validate();
        } catch (Exception e) {
            assert e instanceof InvalidFieldException;
        }

        so.setProp("is mandatory also here");

        try {
            assert ve.validate();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    public void NumericConstraint() throws Exception{
        SONumeric son = new SONumeric();
        assert new ValidateEvaluator<>(son).validate(); // primitives types do not require to be instantiated.
    }

    @Test
    public void PositiveNumericConstraint() throws Exception{
        SONPositive sonp = new SONPositive();
        sonp.setProp(1); // primitive types are initialized to 0.
        assert new ValidateEvaluator<>(sonp).validate(); // primitives types do not require to be instantiated.
    }

    /* OUT OF THE BOX VALIDATION CLASSES */

    @Test
    public void NumericConstraintFails() {
        try {
            SONumericWrapper sonw = new SONumericWrapper();
            assert new ValidateEvaluator<>(sonw).validate();
        } catch (Exception e) {
            assert e instanceof InvalidFieldException;
        }
    }

    @Test
    public void PositiveNumericConstraintFails() {
        try {
            SONPositive sonp = new SONPositive();
            assert new ValidateEvaluator<>(sonp).validate();
        } catch (Exception e) {
            assert e instanceof InvalidFieldException;
        }
    }

    @Test
    public void StringValueConstraintFails() {
        SimpleObject so = new SimpleObject();
        so.setProp("");
        try {
            new ValidateEvaluator<>(so).validate();
        } catch (Exception e) {
            assert e instanceof InvalidFieldException;
        }
    }

    @Test
    public void OctetConstraint() {

    }

}