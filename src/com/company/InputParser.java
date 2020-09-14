package com.company;

import java.util.*;

public class InputParser {
    String lastMethodName = "";
    String lastAssertionMethod = "";
    private String inputToParse;
    private DeclarationInputParser declarationInputParser = new DeclarationInputParser();
    private HelperService helperService = new HelperService();
    private String declarationRegEx ="\\w+\\s\\w+;";
    private String variableDeclarationWithAssignmentRegEx = "\\w+\\s\\w+\\s=\\s\\d+;";
    private String variableAssignmentRegEx = "\\w+\\s=\\s\\d+;";
    private String variableAssignmentToExistingVariableRegEx = "\\w+\\s=\\s\\w+;";
    private String variableDeclarationWithAssignmentToExistingVariableRegEx = "\\w+\\s\\w+\\s=\\s\\w+;";
    private String variableValueCheck = "\\w+";
    private String variableAssigmentToExistingVariableWithExpression = "\\w+\\s=\\s(\\w+((\\s[+]\\s)+|(\\s[-]\\s))+)+\\w+;";
    private String variableDeclarationWithAssigmentToExpression = "\\w+\\s\\w+\\s=\\s(\\w+((\\s[+]\\s)+|(\\s[-]\\s))+)+\\w+;";
    private String methodDeclaration = "(\\w+\\s\\w+)([(]\\s?(\\w+\\s\\w+[,]?\\s?)+[)])\\s[{]";
    private String returnStatment = "return\\s\\w+(\\s?[+|-]?\\s?\\w+?)+?;";
    private String methodInvocation = "\\w+([(]\\w+?(,\\s\\w+)+?[)]);";
    private String variableDeclarationWithAssigmentOfMethodInvocation = "(\\w+\\s\\w+\\s=\\s)(\\w+([(]\\w+?(,\\s\\w+)+?[)]));";
    private String variableAssigmentToMethodInvocationResult = "(\\w+\\s=\\s)(\\w+([(]\\w+?(,\\s\\w+)+?[)]));";
    private String testMethodDeclaration = "(\\w+\\s\\w+)([(][)])\\s[{]";
    private String testAssertion = "assert\\s(\\w+|\\d),\\s\\w+(\\w+([(]\\w+?(,\\s\\w+)+?[)]));";
    private String testMethodInvocation = "test\\s\\w+([(][)]);";
    boolean isStillInMethodDeclaration = false;
    boolean shouldMakeComputatioForMethod = false;
    private String methodParameters = "_\\w+_\\d+";

    public void parse(String input) {
        this.inputToParse = input;
        String[] words = this.inputToParse.split("\\s+");

        if (isMethodDeclaration()) {
            isStillInMethodDeclaration = true;
            String variableName = words[1].substring(0, words[1].indexOf("("));
            lastMethodName = variableName;
            String methodParameters = this.inputToParse
                    .substring(this.inputToParse.indexOf("(") + 1, this.inputToParse.indexOf(")"));

            Method method = new Method(this.declarationInputParser, variableName);

            if (methodParameters.length() <= 2) {
                // single parameter
            } else {
                String[] methodParametersArray = methodParameters.split(",\\s");
                method.declareParameters(methodParametersArray);
            }

            try {
                method.declare();
                // expected method parameter length
                String paramLength = variableName + "_" + "param_length";
                method.declare(paramLength);
                method.assign(paramLength, methodParameters.split(",\\s").length);
            } catch (Exception e) {
                System.out.println(e);
            }
        }else if (isDeclaration() && !words[0].equals("return")) {
            System.out.println("we have declaration here");

            String variableName = words[1].substring(0, words[1].length() - 1);
            Variable variableDeclaration = new Variable(this.declarationInputParser, variableName);

            try {
                if (this.isStillInMethodDeclaration) {
                    //method declaration
                    variableDeclaration.declare(lastMethodName + "_" + variableName);
                } else {
                    variableDeclaration.declare();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        } else if (isDeclarationWithAssigment()) {
            System.out.println("we have assignment here");

            String variableName = words[1];
            int variableValue = Integer.parseInt(words[3].substring(0, words[3].length() - 1));

            try {
                if (this.isStillInMethodDeclaration) {
                    String currentVariableName = lastMethodName + "_" + variableName;
                    Method method = new Method(this.declarationInputParser, currentVariableName);

                    method.declare(currentVariableName);
                    method.assign(currentVariableName, variableValue);
                } else {
                    Variable variable = new Variable(this.declarationInputParser, variableName);
                    variable.declare(variableName);
                    variable.assign(variableName, variableValue);
                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }

        } else if (isAssignment()) {
            String variableName = words[0];
            int variableValue = Integer.parseInt(words[2].substring(0, words[2].length() - 1));

            try {
                if (this.isStillInMethodDeclaration) {
                    String currentVariableName = lastMethodName + "_" + variableName;
                    Method method = new Method(this.declarationInputParser, currentVariableName);
                    method.assign(currentVariableName, variableValue);
                } else {
                    Variable variable = new Variable(this.declarationInputParser, variableName);
                    variable.assign(variableName, variableValue);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else if (isExistingVariableAssignmentToExistingVariable()) {
            String newVariableName = words[0];
            String existingVariableName = words[2].substring(0, words[2].length() - 1);
            // might have an issue
            try {
                if (isStillInMethodDeclaration) {
                    String newMethodVariableName = lastMethodName + "_" + newVariableName;
                    String existingMethodVariableName = lastMethodName + "_" + existingVariableName;

                    Method method = new Method(this.declarationInputParser, newMethodVariableName);

                    int existingVariableValue = method.getVariableValue(existingMethodVariableName);
                    method.assign(newMethodVariableName, existingVariableValue);
                } else {
                    Variable variable = new Variable(this.declarationInputParser, newVariableName);
                    int existingVariableValue = variable.getVariableValue(existingVariableName);
                    variable.assign(newVariableName, existingVariableValue);
                }
            } catch (Exception e) {
                System.out.println("Such variable does not exist");
            }
        } else if (isDeclarationWithAssigmentToExistingVariable()) {
            String newVariableName = words[1];
            String existingVariableName = words[3].substring(0, words[3].length() - 1);

            try {
                if (isStillInMethodDeclaration) {
                    String existingMethodVariableName = lastMethodName + "_" + words[3].substring(0, words[3].length() - 1);
                    String newMethodVariableName = lastMethodName + "_" + newVariableName;
                    Method method = new Method(this.declarationInputParser, newMethodVariableName);
                    int existingVariableValue = this.declarationInputParser.getVariableValue(existingMethodVariableName);

                    method.declare(newMethodVariableName);
                    method.assign(newMethodVariableName, existingVariableValue);
                } else {
                    int existingVariableValue = this.declarationInputParser.getVariableValue(existingVariableName);
                    Variable variable = new Variable(this.declarationInputParser, newVariableName);
                    variable.declare(newVariableName);

                    variable.assign(newVariableName, existingVariableValue);
                }

            } catch (Exception e) {
                System.out.println("Such variable does not exist");
            }
        } else if (isVariableCheck() && !this.isStillInMethodDeclaration) {
            String variableName = words[0];
            try {
                int existingVariableValue = this.declarationInputParser.getVariableValue(variableName);
                System.out.println(existingVariableValue);
            } catch (Exception e) {
                System.out.println("Such variable does not exist");
            }
        } else if (isVariableAssigmentToExistingVariableWithExpression()) {
            String variableName = words[0];
            // remove semicolon
            words[words.length - 1] = words[words.length - 1].substring(0, words[words.length - 1].length() - 1);

            String[] expression = helperService.getSliceOfArray(words, 2, words.length);
            if (isStillInMethodDeclaration) {
                String methodVariableName = lastMethodName + "_" + variableName;

                this.performComputation(methodVariableName, expression);
            } else {
                this.performComputation(variableName, expression);
            }

        } else if(isVariableDeclarationWithAssigmentOfExpression()) {
            String variableName = words[1];
            words[words.length - 1] = words[words.length - 1].substring(0, words[words.length - 1].length() - 1);
            String[] expression = helperService.getSliceOfArray(words, 3, words.length);

            try {
                if (isStillInMethodDeclaration) {
                    String methodVariableName = lastMethodName + "_" + variableName;
                    // int sum(int a, int b) {
                    for (int i = 0; i < expression.length; i++) {
                        // this mutation is not so good ...
                        String currentElement = expression[i];

                        if (!currentElement.equals("+") && !currentElement.equals("-") && !helperService.isNumber(expression[i])) {
                            expression[i] = lastMethodName + "_" + currentElement;
                        }
                    }
                    Method method = new Method(this.declarationInputParser, methodVariableName);

                    method.declare(methodVariableName);
                    this.performComputation(methodVariableName, expression);
                } else  {
                    Variable variable = new Variable(this.declarationInputParser, variableName);

                    variable.declare(variableName);
                    this.performComputation(variableName, expression);
                }
            } catch (Exception e) {
                System.out.println("Such variable does not exist");
            }
        } else if (endOfMethodDeclaration()) {
            this.isStillInMethodDeclaration = false;
            this.lastMethodName = "";
        } else if (isMethodReturnStatment()) {
            if (isStillInMethodDeclaration) {
                // skipping return and space
                String expression = this.inputToParse.substring(7, this.inputToParse.length() - 1);

                try {
                    Method method = new Method(this.declarationInputParser, lastMethodName);
                    method.assign(lastMethodName, expression);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } else if (isMethodInvocation()) {
            String methodName = words[0].substring(0, words[0].indexOf("("));
            String[] parameters = this.inputToParse.substring(this.inputToParse.indexOf("(") + 1, this.inputToParse.indexOf(")")).split(",\\s");

            this.methodInvocation(methodName, parameters);

            this.performMethodInvocation(methodName);

        } else if (isVariableDeclarationWithAssigmentOfMethodInvocation()) {
            String variableName = words[1];
            String methodName = words[3].substring(0, words[3].indexOf("("));
            String[] parameters = this.inputToParse.substring(this.inputToParse.indexOf("(") + 1, this.inputToParse.indexOf(")")).split(",\\s");

            this.methodInvocation(methodName, parameters);

            this.performMethodInvocation(methodName);

            try {

                int resultOfMethodInvocation = this.declarationInputParser.getVariableValue("result_" + methodName);
                Variable variable = new Variable(this.declarationInputParser, variableName);

                variable.declare(variableName);
                variable.assign(variableName, resultOfMethodInvocation);
            } catch (Exception e) {
                System.out.println("Something happened during method invocation calculations");
            }
        } else if (isVariableAssigmentToMethodInvocationResult()) {
            String variableName = words[0];
            String methodName = words[2].substring(0, words[2].indexOf("("));
            String[] parameters = this.inputToParse.substring(this.inputToParse.indexOf("(") + 1, this.inputToParse.indexOf(")")).split(",\\s");

            this.methodInvocation(methodName, parameters);

            this.performMethodInvocation(methodName);

            try {

                int resultOfMethodInvocation = this.declarationInputParser.getVariableValue("result_" + methodName);
                Variable variable = new Variable(this.declarationInputParser, variableName);

                variable.assign(variableName, resultOfMethodInvocation);

            } catch (Exception e) {
                System.out.println("No such variable found");
            }
        } else if (isTestMethodDeclaration()) {
            isStillInMethodDeclaration = true;
            String variableName = words[1].substring(0, words[1].indexOf("("));
            this.lastMethodName = variableName;
            this.lastAssertionMethod = variableName;

            try {
                this.declarationInputParser.declare(variableName);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (isTestAssertion()) {
            String variableToAssert = words[1].substring(0, words[1].length() - 1);
            String methodName = words[2].substring(0, words[2].indexOf("("));
            String[] parameters = this.inputToParse.substring(this.inputToParse.indexOf("(") + 1, this.inputToParse.indexOf(")")).split(",\\s");
            try {
                this.methodInvocation(methodName, parameters);
                this.performMethodInvocation(methodName);
                int resultOfMethodInvocation = this.declarationInputParser.getVariableValue("result_" + methodName);
                Method method = new Method(this.declarationInputParser, lastAssertionMethod);

                if (helperService.isNumber(variableToAssert)){
                    if (Integer.parseInt(variableToAssert) == resultOfMethodInvocation) {
                        try {

                            method.assign(lastAssertionMethod,  lastAssertionMethod + " runs successfully");
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
                        try {
                            method.assign(lastAssertionMethod,  lastAssertionMethod + " fails");
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                } else {
                    try {
                        int varValue = this.declarationInputParser.getVariableValue(lastAssertionMethod + "_" + variableToAssert);

                        if (varValue == resultOfMethodInvocation) {
                            try {
                                method.assign(lastAssertionMethod,  lastAssertionMethod + " runs successfully");
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        } else {
                            try {
                                method.assign(lastAssertionMethod,  lastAssertionMethod + " fails");
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    } catch (Exception e2) {
                        System.out.println(e2);
                    }
                }
                lastAssertionMethod = "";
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (isTestMethodInvocation()) {
            String testMethodToInvoke = words[1].substring(0, words[1].indexOf("("));

            try {
                int methodValue = this.declarationInputParser.getVariableValue(testMethodToInvoke);
                System.out.println(this.declarationInputParser.methodExpressionValue(methodValue));
            } catch (Exception e) {

            }
        }
    }


    private void performMethodInvocation(String methodName) {
        try {
            int methodExpreessionValue = this.declarationInputParser.getVariableValue(methodName);
            String methodExpressionToCompute = this.declarationInputParser.methodExpressionValue(methodExpreessionValue);
            String methodResultVariableName = "result_" + methodName;
            try {
                this.declarationInputParser.getVariableValue(methodResultVariableName);
            } catch (Exception e) {
                this.declarationInputParser.declare(methodResultVariableName);
            }

            String[] expressions = methodExpressionToCompute.split(" ");
            this.shouldMakeComputatioForMethod = true;
            this.lastMethodName = methodName;
            this.performComputation(methodResultVariableName, expressions);
        } catch (Exception e) {
            this.shouldMakeComputatioForMethod = false;
            System.out.println("NO variable found");
        }

        this.shouldMakeComputatioForMethod = false;
        this.lastMethodName = "";
    }
    private void methodInvocation(String methodName, String[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            String parameter = parameters[i];

            Set<String> declarationKeys = this.declarationInputParser.getAllDeclarationKeys();
            for (String declarationKey: declarationKeys) {
                String valueOfCurrentIterationIndex = (String.valueOf(i));
                String valueOfParameterSuffix = declarationKey.split("_")[declarationKey.split("_").length - 1];

                if (declarationKey.matches(methodName + methodParameters) && valueOfCurrentIterationIndex.equals(valueOfParameterSuffix)){
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

    private void performComputation(String variableName, String[] expression) {
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

    private boolean isDeclaration() {
        return this.inputToParse.matches(declarationRegEx);
    }
    private boolean isDeclarationWithAssigment() {
        return this.inputToParse.matches(variableDeclarationWithAssignmentRegEx);
    }
    private boolean isAssignment() {
        return this.inputToParse.matches(variableAssignmentRegEx);
    }
    private boolean isExistingVariableAssignmentToExistingVariable() {
        return this.inputToParse.matches(variableAssignmentToExistingVariableRegEx);
    }
    private boolean isDeclarationWithAssigmentToExistingVariable() {
        return this.inputToParse.matches(variableDeclarationWithAssignmentToExistingVariableRegEx);
    }
    private boolean isVariableCheck() {
        return this.inputToParse.matches(variableValueCheck);
    }
    private boolean isVariableAssigmentToExistingVariableWithExpression() {
        return this.inputToParse.matches(variableAssigmentToExistingVariableWithExpression);
    }
    private boolean isVariableDeclarationWithAssigmentOfExpression() {
        return this.inputToParse.matches(variableDeclarationWithAssigmentToExpression);
    }
    private boolean endOfMethodDeclaration() {
        return this.inputToParse.matches("[}]");
    }
    private boolean isMethodDeclaration() {
        return this.inputToParse.matches(methodDeclaration);
    }
    private boolean isMethodReturnStatment() {
        return this.inputToParse.matches(returnStatment);
    }
    private boolean isMethodInvocation() {
        return this.inputToParse.matches(methodInvocation);
    }
    private boolean isVariableDeclarationWithAssigmentOfMethodInvocation() {
        return this.inputToParse.matches(variableDeclarationWithAssigmentOfMethodInvocation);
    }
    private boolean isVariableAssigmentToMethodInvocationResult() {
        return this.inputToParse.matches(variableAssigmentToMethodInvocationResult);
    }
    private boolean isTestMethodDeclaration() {
        return this.inputToParse.matches(testMethodDeclaration);
    }
    private boolean isTestAssertion() {
        return this.inputToParse.matches(testAssertion);
    }
    private boolean isTestMethodInvocation() {
        return this.inputToParse.matches(testMethodInvocation);
    }
}
