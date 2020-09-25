package com.company;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DeclarationInputParser {
    private Map<String, Integer> declarations = new HashMap<String, Integer>();
    private Map<Integer, String> methodReturns = new HashMap<Integer, String>();

    public void declare(String variable) throws Exception {
        if (!this.declarations.containsKey(variable)) {
            this.declarations.put(variable, null);

            System.out.println(this.declarations);
        } else {
            throw new Exception("Variable" + variable + " has already been declared.");
        }
    }

    public void assign(String variable, int value) throws Exception {
        if (this.declarations.containsKey(variable)) {
            this.addAssignmentToVariable(variable, value);
        } else {
            throw new Exception("Variable " + variable + " is not declared.");
        }
    }

    public void assign(String variable, String value) throws Exception {
        if (this.declarations.containsKey(variable)) {
            int id = (int)(new Date().getTime());
            this.addAssignmentToVariable(variable, id);
            this.methodReturns.put(id, value);
        } else {
            throw new Exception("Variable " + variable + " is not declared.");
        }
    }

    public void addAssignmentToVariable(String variableName, int value) {
        this.declarations.replace(variableName, value);

        System.out.println(this.declarations);
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
