package com.company;

import java.util.*;

public class InputParser {
    String lastMethodName = "";
    String lastAssertionMethod = "";
    private String inputToParse;
    private DeclarationInputParser declarationInputParser = new DeclarationInputParser();
    private HelperService helperService;
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

            try {
                if (this.isStillInMethodDeclaration) {
                    Method method = new Method(this.declarationInputParser, lastMethodName + "_" + variableName);

                    method.declare(lastMethodName + "_" + variableName);
                } else {
                    Variable variable = new Variable(this.declarationInputParser);

                    variable.declare(variableName);
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
                    Variable variable = new Variable(this.declarationInputParser);
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
                    Variable variable = new Variable(this.declarationInputParser);
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
                    Variable variable = new Variable(this.declarationInputParser);
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
                    Variable variable = new Variable(this.declarationInputParser);
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
                Method method = new Method(this.declarationInputParser, methodVariableName);
                method.performComputation(methodVariableName, expression, lastMethodName, shouldMakeComputatioForMethod);
            } else {
                Method method = new Method(this.declarationInputParser, variableName);

                method.performComputation(variableName, expression, lastMethodName, shouldMakeComputatioForMethod);
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
                    method.performComputation(methodVariableName, expression, lastMethodName, shouldMakeComputatioForMethod);
                } else  {
                    Variable variable = new Variable(this.declarationInputParser);
                    Method method = new Method(this.declarationInputParser, variableName);

                    variable.declare(variableName);
                    method.performComputation(variableName, expression, lastMethodName, shouldMakeComputatioForMethod);

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
            Method method = new Method(this.declarationInputParser, methodName);

            method.methodInvocation(methodName, parameters);
            this.lastMethodName = methodName;
            this.shouldMakeComputatioForMethod = true;

            method.performMethodInvocation(methodName, shouldMakeComputatioForMethod);

            this.shouldMakeComputatioForMethod = false;
            this.lastMethodName = "";

        } else if (isVariableDeclarationWithAssigmentOfMethodInvocation()) {
            String variableName = words[1];
            String methodName = words[3].substring(0, words[3].indexOf("("));
            String[] parameters = this.inputToParse.substring(this.inputToParse.indexOf("(") + 1, this.inputToParse.indexOf(")")).split(",\\s");
            Method method = new Method(this.declarationInputParser, methodName);

            method.methodInvocation(methodName, parameters);
            this.lastMethodName = methodName;
            this.shouldMakeComputatioForMethod = true;

            method.performMethodInvocation(methodName, shouldMakeComputatioForMethod);

            try {

                int resultOfMethodInvocation = this.declarationInputParser.getVariableValue("result_" + methodName);
                Variable variable = new Variable(this.declarationInputParser);

                variable.declare(variableName);
                variable.assign(variableName, resultOfMethodInvocation);
            } catch (Exception e) {
                this.shouldMakeComputatioForMethod = false;
                this.lastMethodName = "";
                System.out.println("Something happened during method invocation calculations");
            }
        } else if (isVariableAssigmentToMethodInvocationResult()) {
            String variableName = words[0];
            String methodName = words[2].substring(0, words[2].indexOf("("));
            String[] parameters = this.inputToParse.substring(this.inputToParse.indexOf("(") + 1, this.inputToParse.indexOf(")")).split(",\\s");
            Method method = new Method(this.declarationInputParser, methodName);
            method.methodInvocation(methodName, parameters);
            this.lastMethodName = methodName;
            this.shouldMakeComputatioForMethod = true;

            method.performMethodInvocation(methodName, shouldMakeComputatioForMethod);

            try {

                int resultOfMethodInvocation = this.declarationInputParser.getVariableValue("result_" + methodName);
                Variable variable = new Variable(this.declarationInputParser);

                variable.assign(variableName, resultOfMethodInvocation);

            } catch (Exception e) {
                this.shouldMakeComputatioForMethod = false;
                this.lastMethodName = "";
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
                Method method = new Method(this.declarationInputParser, lastAssertionMethod);

                method.methodInvocation(methodName, parameters);
                this.lastMethodName = methodName;
                this.shouldMakeComputatioForMethod = true;

                method.performMethodInvocation(methodName, shouldMakeComputatioForMethod);
                int resultOfMethodInvocation = this.declarationInputParser.getVariableValue("result_" + methodName);

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
