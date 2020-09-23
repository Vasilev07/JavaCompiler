package com.company;

public class DeclarationWithAssignment extends ParserDeclarationWithAssignment {
    private DeclarationInputParser declarationInputParser;
    private String[] words;
    private boolean isStillInMethodDeclaration;
    private String variableName;
    private String lastMethodName;
    private int variableValue;

    public void setLastMethodName(String lastMethodName) {
        this.lastMethodName = lastMethodName;
    }

    public DeclarationWithAssignment(DeclarationInputParser declarationInputParser, String[] words, boolean isStillInMethodDeclaration) {
        this.declarationInputParser = declarationInputParser;
        this.words = words;
        this.isStillInMethodDeclaration = isStillInMethodDeclaration;
    }

    @Override
    void assign() {
        if (this.isStillInMethodDeclaration) {
            String currentVariableName = lastMethodName + "_" + variableName;
            Method method = new Method(this.declarationInputParser);
            method.assign(currentVariableName, variableValue);
        } else {
            Variable variable = new Variable(this.declarationInputParser);
            variable.assign(variableName, variableValue);
        }
    }

    @Override
    void parse(String input) {
        variableName = words[1];
        variableValue = Integer.parseInt(words[3].substring(0, words[3].length() - 1));
    }

    @Override
    void declare() {
        if (this.isStillInMethodDeclaration) {
            String currentVariableName = lastMethodName + "_" + variableName;
            Method method = new Method(this.declarationInputParser);
            method.declare(currentVariableName);
        } else {
            Variable variable = new Variable(this.declarationInputParser);
            variable.declare(variableName);
        }
    }
}
