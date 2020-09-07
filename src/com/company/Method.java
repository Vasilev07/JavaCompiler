package com.company;

public class Method implements Declaration, Assignment {
    private DeclarationInputParser declarationInputParser;
    private String methodName;
    private Variable variableDeclaration;

    public Method(DeclarationInputParser declarationInputParser, String methodName) {
        this.declarationInputParser = declarationInputParser;
        this.methodName = methodName;
        this.variableDeclaration = new Variable(declarationInputParser, methodName);
    }

    @Override
    public void declare() {
        variableDeclaration.declare();
    }

    @Override
    public void declare(String variable) {
        variableDeclaration.declare(variable);
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

    public void declareParameters(String[] methodParametersArray) {
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
