package com.company;

public class Declaration extends ParserDeclaration {
    private DeclarationInputParser declarationInputParser;
    private String[] words;
    private boolean isStillInMethodDeclaration;
    private String variableName;
    private String lastMethodName;

    public void setLastMethodName(String lastMethodName) {
        this.lastMethodName = lastMethodName;
    }

    public Declaration(DeclarationInputParser declarationInputParser, String[] words, boolean isStillInMethodDeclaration){
        this.declarationInputParser = declarationInputParser;
        this.words = words;
        this.isStillInMethodDeclaration = isStillInMethodDeclaration;
    }

    @Override
    void parse(String input) {
        variableName = words[1].substring(0, words[1].length() - 1);
    }
    @Override
    void declare() {
        if (this.isStillInMethodDeclaration) {
            Method method = new Method(this.declarationInputParser);

            method.declare(lastMethodName + "_" + variableName);
        } else {
            Variable variable = new Variable(this.declarationInputParser);

            variable.declare(variableName);
        }
    }
}
