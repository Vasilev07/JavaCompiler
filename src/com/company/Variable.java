package com.company;

public class Variable implements Declaration, Assignment {
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

    @Override
    public void declare(String variable) {
        try {
            declarationInputParser.declare(variable);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void assign(String variable, int value) {
        try {
            this.declarationInputParser.assign(variable, value);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void assign(String variable, String value) {
        try {
            this.declarationInputParser.assign(variable, value);
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
