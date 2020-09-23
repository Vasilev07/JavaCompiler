package com.company;

public class ExistingVariableAssignmentToExistingVariable extends ParserAssignment {
    private String existingVariableName;
    private String newVariableName;
    private String lastMethodName;
    private DeclarationInputParser declarationInputParser;
    protected String[] words;
    private boolean isStillInMethodDeclaration;

    public ExistingVariableAssignmentToExistingVariable(DeclarationInputParser declarationInputParser, String[] words, boolean isStillInMethodDeclaration) {
        this.declarationInputParser = declarationInputParser;
        this.words = words;
        this.isStillInMethodDeclaration = isStillInMethodDeclaration;
    }

    public void setLastMethodName(String lastMethodName) {
        this.lastMethodName = lastMethodName;
    }

    @Override
    void parse(String input) {
        newVariableName = words[0];
        existingVariableName = words[2].substring(0, words[2].length() - 1);
    }

    @Override
    void assign() throws Exception {
        if (isStillInMethodDeclaration) {
            String newMethodVariableName = lastMethodName + "_" + newVariableName;
            String existingMethodVariableName = lastMethodName + "_" + existingVariableName;

            Method method = new Method(this.declarationInputParser);

            int existingVariableValue = method.getVariableValue(existingMethodVariableName);
            method.assign(newMethodVariableName, existingVariableValue);
        } else {
            Variable variable = new Variable(this.declarationInputParser);
            int existingVariableValue = variable.getVariableValue(existingVariableName);
            variable.assign(newVariableName, existingVariableValue);
        }
    }
}
