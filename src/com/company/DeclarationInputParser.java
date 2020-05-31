package com.company;

import java.util.*;

public class DeclarationInputParser {
    private HashMap<String, Integer> declarations = new HashMap<String, Integer>();
    private Declarations currentDeclaration = new Declarations();

    public void declare(String variable) {
        if (!this.declarations.containsKey(variable)) {
            currentDeclaration.add(variable);

            this.declarations = this.currentDeclaration.getDeclarations();
        } else {
            System.out.println("Variable " + variable + " has already been declared.");
        }
    }

    public void assign(String variable, int value) {
        if (this.declarations.containsKey(variable)) {
            this.currentDeclaration.addAssignmentToVariable(variable, value);
        } else {
            System.out.println("Variable " + variable + " is not declared.");
        }
    }
}
