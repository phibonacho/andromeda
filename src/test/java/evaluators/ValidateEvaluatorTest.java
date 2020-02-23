package evaluators;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import it.phibonachos.andromeda.ValidateEvaluator;
import it.phibonachos.andromeda.exception.ConflictFieldException;
import it.phibonachos.andromeda.exception.CyclicRequirementException;
import it.phibonachos.andromeda.exception.InvalidFieldException;
import it.phibonachos.andromeda.exception.RequirementsException;
import evaluators.validate_evaluator_classes.*;

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
            assert e instanceof InvalidFieldException;
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
    public void cyclicRequirements() {
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
    public void cascadeRequirements() {
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
    public void cascadeAlternatives() {
        try {
            CascadeAlternativeObject cao = new CascadeAlternativeObject();
            cao.setAProp1("at least this must be set");

            assert new ValidateEvaluator<>(cao).validate();
        } catch (Exception e){
            e.printStackTrace();
            assert e instanceof InvalidFieldException;
            return;
        }
        assert false;
    }

    @Test
    public void listCollectionTest() throws Exception {
        CollectionObject co = new CollectionObject();
        List<SimpleObject> l = new ArrayList<>();
        co.setMyPrivateList(l);
        ValidateEvaluator<CollectionObject> ve = new ValidateEvaluator<>(co);
        assert ve.validate();
    }

}