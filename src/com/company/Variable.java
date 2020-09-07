package com.company;

public class Variable implements Declaration {
    private DeclarationInputParser declarationInputParser;
    private String variableName;

    public Variable(DeclarationInputParser declarationInputParser, String variableName) {
        this.declarationInputParser = declarationInputParser;
        this.variableName = variableName;
    }

    @Override
    public void declare() {
        try {
            declarationInputParser.declare(variableName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void declare(String variable) {
        try {
            declarationInputParser.declare(variable);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                ", variableName='" + variableName + '\'' +
                '}';
    }
}
