package com.company;

public class DeclarationWithAssignment {
    public void perform(Object obj, String name, int value) {
        if (obj instanceof Variable) {
            performOnVariable((Variable) obj, name, value);
        } else {
            performOnMethod((Method) obj, name, value);
        }
    }

    private void performOnVariable(Variable variable, String name, int value) {
        variable.declare(name);
        variable.assign(name, value);
    }

    private void performOnMethod(Method method, String name, int value) {
        method.declare(name);
        method.assign(name, value);
    }
}
