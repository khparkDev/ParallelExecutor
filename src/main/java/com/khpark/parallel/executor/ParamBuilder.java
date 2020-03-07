package com.khpark.parallel.executor;

import java.util.ArrayList;
import java.util.List;

public class ParamBuilder {
    private List<Object> parameterList;

    public ParamBuilder() {
        parameterList = new ArrayList<>();
    }

    public List<Object> getParameterList() {
        return parameterList;
    }

    public ParamBuilder add(Object obj) {
        parameterList.add(obj);
        return this;
    }
}
