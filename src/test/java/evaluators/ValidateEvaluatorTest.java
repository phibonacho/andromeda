package evaluators;


import evaluators.validate_evaluator_classes.*;
import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class ValidateEvaluatorTest {

    /* POSITIVE TEST */

    @Test
    public void plainValidation() throws Exception {
        SimpleObject so = new SimpleObject();
        so.setProp("this is a valid string as it is not blank");

        assert new ValidateEvaluator<>(so).validate();
    }

    @Test
    public void plainFailValidation() throws Exception {
        SimpleObject so = new SimpleObject();
        try {
            assert new ValidateEvaluator<>(so).validate();
        } catch(Exception e) {
            assert true;
            return;
        }

        assert false;
    }

    @Test
    public void validationAlternative() throws Exception {
        AlternativeObject ao = new AlternativeObject();
        ao.setAlternativeProp("this is a valid alternative string");

        assert new ValidateEvaluator<>(ao).validate();
    }

    @Test
    public void validationWithRequirements() throws Exception {
        RequirementsObject ro = new RequirementsObject();
        ro.setProp("this property require another field initialized to be parsed as valid");
        ro.setRequiredProp(12d);

        assert new ValidateEvaluator<>(ro).validate();
    }

    @Test
    public void validationWithConflicts() throws Exception {
        ConflictsObject co = new ConflictsObject();
        co.setConflictProp("this property pass validation as its conflicts is not set");

        assert new ValidateEvaluator<>(co).validate();
    }

    @Test
    public void successfulCompoundConstraint() {
        CompoundConstraintObject cco = new CompoundConstraintObject();
        ValidateEvaluator<CompoundConstraintObject> ve = new ValidateEvaluator<>(cco);
        cco.setProp("this is mandatory");
        cco.setProp1(true);
        cco.setProp2("this is required since prop1 is true");

        assert ve.validate();

        try {
            cco.setProp1(false);
            ve.validate();
        } catch (Exception ife) {
            assert ife instanceof InvalidFieldException;
            assert ife.getMessage().contains("incompatible values");
        }

        cco.setProp2(null);

        assert ve.validate();
    }

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
        ValidateEvaluator<NestedObject> ve = new ValidateEvaluator<>(no);
        no.setProp("this is mandatory");

        assert ve.validate();

        SimpleObject so = new SimpleObject();
        no.setSo(so);
        try {
            ve.validate();
        } catch (Exception e) {
            assert e instanceof InvalidFieldException;
        }

        so.setProp("is mandatory also here");

        assert ve.validate();
    }

    @Test
    public void NumericConstraint() {
        SONumeric son = new SONumeric();
        assert new ValidateEvaluator<>(son).validate(); // primitives types do not require to be instantiated.
    }

    @Test
    public void PositiveNumericConstraint() {
        SONPositive sonp = new SONPositive();
        sonp.setProp(1); // primitive types are initialized to 0.
        assert new ValidateEvaluator<>(sonp).validate(); // primitives types do not require to be instantiated.
    }

    /* NEGATIVE TEST */


    @Test
    public void plainValidationFail() {
        try {
            SimpleObject so = new SimpleObject();
            assert new ValidateEvaluator<>(so).validate();
        } catch (Exception ife) {
            assert ife instanceof InvalidFieldException;
            return;
        }
        assert false;
    }

    @Test
    public void validationAlternativeFail() {
        try {
            AlternativeObject ao = new AlternativeObject();
            assert new ValidateEvaluator<>(ao).validate();
        } catch (Exception e) {
            assert e instanceof NoAlternativeException;
            return;
        }
        assert false;
    }

    @Test
    public void validationWithRequirementsFail() {
        try {
            FailRequirementsObject fro = new FailRequirementsObject();
            fro.setProp("this property require another field initialized to be parsed as valid");
            assert new ValidateEvaluator<>(fro).validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
            return;
        }
        assert false;
    }

    @Test
    public void validationWithConflictsFail() {
        try {
            ConflictsObject co = new ConflictsObject();
            co.setProp("this prop conflicts with conflictProp");
            co.setConflictProp("this prop conflict with prop");
            assert new ValidateEvaluator<>(co).validate();
        } catch (Exception e) {
            assert e instanceof ConflictFieldException;
            return;
        }
        assert false;
    }

    /**
     * If a node n1 require a second one n2, n2 cannot have n1 as its own dependency
     */
    @Test
    public void cyclicRequirementsFails() {
        try {
            CyclicRequirementsObject cro = new CyclicRequirementsObject();
            cro.setProp("require prop1");
            cro.setProp1("require prop");
            assert new ValidateEvaluator<>(cro).validate();
        } catch (Exception e){
            assert e instanceof CyclicRequirementException;
            return;
        }
        assert false;
    }

    /**
     * Required nodes inherits parent mandatoriness
     */
    @Test
    public void cascadeRequirementsFails() {
        CascadeRequirementsObject cro = new CascadeRequirementsObject();
        ValidateEvaluator<CascadeRequirementsObject> evaluator = new ValidateEvaluator<>(cro);
        cro.setProp("this is a mandatory property");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }

        cro.setReq1("this is a field required from prop");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }

        cro.setReq2("this is a field required from req1");

        try {
            assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }

        cro.setReq3("this is a field required from req2");

        try {
           assert evaluator.validate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
        }
    }

    /**
     * Child method alternatives are discarded
     */
    @Test
    public void cascadeAlternativesFails() {
        try {
            CascadeAlternativeObject cao = new CascadeAlternativeObject();
            cao.setAProp1("at least this must be set");

            assert new ValidateEvaluator<>(cao).validate();
        } catch (Exception e){
            assert e instanceof NoAlternativeException;
            return;
        }
        assert false;
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

}