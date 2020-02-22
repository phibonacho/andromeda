package phb.testClasses.validate_evaluator_classes;

import phb.annotation.validate.Validate;
import phb.annotation.validate.types.AbstractCollectionType;

import java.util.List;

public class CollectionObject {
    private List<SimpleObject> myPrivateList;

    @Validate(with = AbstractCollectionType.class, mandatory = true)
    public List<SimpleObject> getMyPrivateList() {
        return myPrivateList;
    }

    public void setMyPrivateList(List<SimpleObject> lso) {
        this.myPrivateList = lso;
    }
}
