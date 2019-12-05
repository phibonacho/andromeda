package com.testClasses.validate_evaluator_classes;

import com.annotation.validate.Validate;
import com.annotation.validate.types.AbstractCollectionType;
import com.annotation.validate.types.StringValue;

import java.util.ArrayList;
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
