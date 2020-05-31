package com.company;

import java.util.*;

public class DeclarationInputParser {
    private HashMap<String, Integer> declarations = new HashMap<String, Integer>();
    private Declarations currentDeclaration = new Declarations();

    public void declare(String variable) throws Exception {
        if (!this.declarations.containsKey(variable)) {
            currentDeclaration.add(variable);

            this.declarations = this.currentDeclaration.getDeclarations();
        } else {
            throw new Exception("Variable" + variable + " has already been declared.");
//            System.out.println("Variable " + variable + " has already been declared.");
        }
    }

    public void assign(String variable, int value) throws Exception {
        if (this.declarations.containsKey(variable)) {
            this.currentDeclaration.addAssignmentToVariable(variable, value);
        } else {
            throw new Exception("Variable " + variable + " is not declared.");
        }
    }
}
