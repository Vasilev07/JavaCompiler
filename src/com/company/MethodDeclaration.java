package com.company;

public class MethodDeclaration extends Parser {
    private DeclarationInputParser declarationInputParser;
    private String methodParameters;
    private String[] words;
    private String methodName;

    public String getMethodName() {
        return methodName;
    }

    public MethodDeclaration(DeclarationInputParser declarationInputParser, String[] words) {
        this.declarationInputParser = declarationInputParser;
        this.words = words;
    }

    @Override
    void parse(String input) {
        methodName = words[1].substring(0, words[1].indexOf("("));
        methodParameters = input
                .substring(input.indexOf("(") + 1, input.indexOf(")"));
    }

    @Override
    void assign() {
    }

    @Override
    void declare() {
        Method method = new Method(this.declarationInputParser);
        String[] methodParametersArray = methodParameters.split(",\\s");
        method.declareParameters(methodParametersArray, getMethodName());
    }
}
