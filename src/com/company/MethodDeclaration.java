package com.company;

public class MethodDeclaration implements Declaration {
    private DeclarationInputParser declarationInputParser;
    private String methodName;
    private VariableDeclaration variableDeclaration;

    public MethodDeclaration(DeclarationInputParser declarationInputParser, String methodName) {
        this.declarationInputParser = declarationInputParser;
        this.methodName = methodName;
        this.variableDeclaration = new VariableDeclaration(declarationInputParser, methodName);
    }

    @Override
    public void declare() throws Exception {
        variableDeclaration.declare();
    }

    public void declare(String variable) {
        variableDeclaration.declare(variable);
    }

    public void declareMethodParameters(String[] methodParametersArray) {
        for (int i = 0; i < methodParametersArray.length; i++) {
            String token = methodParametersArray[i];
            try {
                String[] tokens = token.split("\\s");
                String methodParameterName = methodName + "_" + tokens[1] + "_" + i;

                variableDeclaration.declare(methodParameterName);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
