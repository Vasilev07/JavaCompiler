package com.company;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DeclarationInputParser {
    private HashMap<String, Integer> declarations = new HashMap<String, Integer>();
    private Declarations currentDeclaration = new Declarations();
    private HashMap<Integer, String> methodReturns = new HashMap<Integer, String>();
    public void declare(String variable) throws Exception {
        if (!this.declarations.containsKey(variable)) {
            this.currentDeclaration.add(variable);

            this.declarations = this.currentDeclaration.getDeclarations();
        } else {
            throw new Exception("Variable" + variable + " has already been declared.");
        }
    }

    public void assign(String variable, int value) throws Exception {
        if (this.declarations.containsKey(variable)) {
            this.currentDeclaration.addAssignmentToVariable(variable, value);
        } else {
            throw new Exception("Variable " + variable + " is not declared.");
        }
    }

    public void assign(String variable, String value) throws Exception {
        if (this.declarations.containsKey(variable)) {
            int id = (int)(new Date().getTime());
            this.currentDeclaration.addAssignmentToVariable(variable, id);
            this.methodReturns.put(id, value);
        } else {
            throw new Exception("Variable " + variable + " is not declared.");
        }
    }

    public String methodExpressionValue(int uniqueValue) throws Exception {
        if (this.methodReturns.containsKey(uniqueValue)) {
            return this.methodReturns.get(uniqueValue);
        } else {
            throw new Exception("Method return expression does not exist");
        }
    }

    public int getVariableValue(String variableName) throws Exception {
        if (this.declarations.containsKey(variableName)) {
            return this.declarations.get(variableName);
        } else {
            throw new Exception("Variable " + variableName + " is not declared.");
        }
    }

    public Set<String> getAllDeclarationKeys() {
        return this.declarations.keySet();
    }


}
