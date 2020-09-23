package com.company;

public class Assignment extends ParserAssignment {
    private DeclarationInputParser declarationInputParser;
    protected String[] words;
    private boolean isStillInMethodDeclaration;
    private String variableName;
    private int variableValue;
    private String lastMethodName;

    public void setLastMethodName(String lastMethodName) {
        this.lastMethodName = lastMethodName;
    }

    public Assignment(DeclarationInputParser declarationInputParser, String[] words, boolean isStillInMethodDeclaration){
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
        variableName = words[0];
        variableValue = Integer.parseInt(words[2].substring(0, words[2].length() - 1));
    }
}
