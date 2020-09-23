package com.company;

public class Variable implements DeclarationInterface, AssignmentInterface {
    private DeclarationInputParser declarationInputParser;

    public Variable(DeclarationInputParser declarationInputParser) {
        this.declarationInputParser = declarationInputParser;
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

    public int getVariableValue(String existingMethodVariableName) throws Exception {
        try {
            return this.declarationInputParser.getVariableValue(existingMethodVariableName);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public String toString() {
        return "";
    }
}
