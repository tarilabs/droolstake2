package org.drools.clp;

import org.drools.clp.valuehandlers.FunctionCaller;

public interface Function { //extends ValueHandler {
    public ValueHandler execute(ValueHandler[] args,
                                ExecutionContext context);

    public String getName();

    public void initCallback(ExecutionBuildContext context);

    public ValueHandler addParameterCallback(int index,
                                             FunctionCaller caller,
                                             ValueHandler valueHandler,                                             
                                             ExecutionBuildContext context);

    public LispList createList(int index);
}
