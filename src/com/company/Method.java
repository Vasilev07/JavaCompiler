package com.company;

import java.util.Set;

public class Method implements Declaration, Assignment {
    private DeclarationInputParser declarationInputParser;
    private HelperService helperService = new HelperService();
    private String methodName;
    private Variable variableDeclaration;

    public Method(DeclarationInputParser declarationInputParser, String methodName) {
        this.declarationInputParser = declarationInputParser;
        this.methodName = methodName;
        this.variableDeclaration = new Variable(declarationInputParser);
    }

    @Override
    public void declare(String variable) {
        try {
            variableDeclaration.declare(variable);
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

    public void performComputation(String variableName, String[] expression, String lastMethodName, boolean shouldMakeComputatioForMethod) {
        int result;
        String initVariable = "";
        try {
            if (!shouldMakeComputatioForMethod) {
                result = this.declarationInputParser.getVariableValue(expression[0]);
            } else {
                throw new Exception("we have to perform actions for method invocation");
            }
        } catch (Exception e) {
            try {
                result = Integer.parseInt(expression[0]);
            } catch (Exception e2) {
                try {
                    String variable = "";
                    for (String key: this.declarationInputParser.getAllDeclarationKeys()) {
                        if (key.matches(lastMethodName + "_" + expression[0] + "_\\d")) {
                            variable = key;
                            initVariable = key;
                            break;
                        }
                    }
                    result = this.declarationInputParser.getVariableValue(variable);
                } catch (Exception e3) {
                    System.out.println(e3);
                    result = 0;
                }
            }
        }

        String previousSign = "";
        int currentNumber = 0;

        for (int i = 1; i < expression.length; i++) {
            if(i % 2 == 0) {
                try {
                    new java.math.BigInteger(expression[i]);
                    //it is number
                    currentNumber = Integer.parseInt(expression[i]);
                } catch (NumberFormatException e) {
                    // it is variable
                    try {
                        if (shouldMakeComputatioForMethod) {
                            String variable = "";
                            for (String key: this.declarationInputParser.getAllDeclarationKeys()) {
                                if (key.matches(lastMethodName + "_" + expression[i] + "_\\d") && !key.equals(initVariable)) {
                                    variable = key;
                                    break;
                                }
                            }
                            currentNumber = this.declarationInputParser.getVariableValue(variable);
                        } else {
                            currentNumber = this.declarationInputParser.getVariableValue(expression[i]);
                        }
                    } catch (Exception ex) {
                        System.out.println("Such variable does not exist");
                    }
                }

                result = helperService.computeExpression(result, currentNumber, previousSign);
            } else {
                // we have sign
                previousSign = expression[i];
            }
        }

        try {
            this.declarationInputParser.assign(variableName, result);
        } catch (Exception e) {
            System.out.println("Such variable does not exist");
        }
    }

    public void performMethodInvocation(String methodName, boolean shouldMakeComputatioForMethod) {
        try {
            int methodExpreessionValue = this.declarationInputParser.getVariableValue(methodName);
            String methodExpressionToCompute = this.declarationInputParser.methodExpressionValue(methodExpreessionValue);
            String methodResultVariableName = methodName;
            try {
                this.declarationInputParser.getVariableValue(methodResultVariableName);
            } catch (Exception e) {
                this.declarationInputParser.declare(methodResultVariableName);
            }

            String[] expressions = methodExpressionToCompute.split(" ");
            this.performComputation(methodResultVariableName, expressions, methodName, shouldMakeComputatioForMethod);
        } catch (Exception e) {
            System.out.println("NO variable found");
        }


    }

    public void methodInvocation(String methodName, String[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            String parameter = parameters[i];

            Set<String> declarationKeys = this.declarationInputParser.getAllDeclarationKeys();
            for (String declarationKey: declarationKeys) {
                String valueOfCurrentIterationIndex = (String.valueOf(i));
                String valueOfParameterSuffix = declarationKey.split("_")[declarationKey.split("_").length - 1];

                if (declarationKey.matches(methodName + "_\\w+_\\d+") && valueOfCurrentIterationIndex.equals(valueOfParameterSuffix)){
                    if(helperService.isNumber(parameters[i])) {
                        try {
                            this.declarationInputParser.assign(declarationKey, Integer.parseInt(parameters[i]));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
                        try {
                            int variableValue = this.declarationInputParser.getVariableValue(parameter);
                            this.declarationInputParser.assign(declarationKey, variableValue);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }
    }
}
