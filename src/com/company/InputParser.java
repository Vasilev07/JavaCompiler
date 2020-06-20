package com.company;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    private String inputToParse;
    private DeclarationInputParser declarationInputParser = new DeclarationInputParser();
    private String declarationRegEx ="\\w+\\s\\w+;";
    private String variableDeclarationWithAssignmentRegEx = "\\w+\\s\\w+\\s=\\s\\d+;";
    private String variableAssignmentRegEx = "\\w+\\s=\\s\\d+;";
    private String variableAssignmentToExistingVariableRegEx = "\\w+\\s=\\s\\w+;";
    private  String variableDeclarationWithAssignmentToExistingVariableRegEx = "\\w+\\s\\w+\\s=\\s\\w+;";
    private String variableValueCheck = "\\w+";
    private String variableAssigmentToExistingVariableWithEpression = "\\w+\\s=\\s(\\w+((\\s[+]\\s)+|(\\s[-]\\s))+)+\\w+;";

    public void parse(String input) {
        this.inputToParse = input;
        String[] words = this.inputToParse.split("\\s+");

        if (isDeclaration()) {
            System.out.println("we have declaration here");

            String variableName = words[1].substring(0, words[1].length() - 1);
            try {
                this.declarationInputParser.declare(variableName);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        } else if (isDeclarationWithAssigment()) {
            System.out.println("we have assignment here");

            String variableName = words[1];
            int variableValue = Integer.parseInt(words[3].substring(0, words[3].length() - 1));

            try {
                this.declarationInputParser.declare(variableName);
                this.declarationInputParser.assign(variableName, variableValue);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        } else if (isAssignment()) {
            String variableName = words[0];
            int variableValue = Integer.parseInt(words[2].substring(0, words[2].length() - 1));

            try {
                this.declarationInputParser.assign(variableName, variableValue);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else if (isExistingVariableAssignmentToExistingVariable()) {
            String newVariableName = words[0];
            String existingVariableName = words[2].substring(0, words[2].length() - 1);

            try {
                int existingVariableValue = this.declarationInputParser.getVariableValue(existingVariableName);

                this.declarationInputParser.assign(newVariableName, existingVariableValue);
            } catch (Exception e) {
                System.out.println("Such variable does not exist");
            }
        } else if (isDeclarationWithAssigmentToExistingVariable()) {
            String newVariableName = words[1];
            String existingVariableName = words[3].substring(0, words[3].length() - 1);

            try {
                int existingVariableValue = this.declarationInputParser.getVariableValue(existingVariableName);
                this.declarationInputParser.declare(newVariableName);

                this.declarationInputParser.assign(newVariableName, existingVariableValue);
            } catch (Exception e) {
                System.out.println("Such variable does not exist");
            }
        } else if (isVariableCheck()) {
            String variableName = words[0];
            try {
                int existingVariableValue = this.declarationInputParser.getVariableValue(variableName);
                System.out.println(existingVariableValue);
            } catch (Exception e) {
                System.out.println("Such variable does not exist");
            }
        } else if (isVariableAssigmentToExistingVariableWithEpression()) {
            String variableName = words[0];
            // remove semicolon
            words[words.length - 1] = words[words.length - 1].substring(0, words[words.length - 1].length() - 1);

            String[] expression = getSliceOfArray(words, 2, words.length);

            int result;
            try {
                result = this.declarationInputParser.getVariableValue(expression[0]);
            } catch (Exception e) {
                result = Integer.parseInt(expression[0]);
            }

            String previousSign = "";
            int currentNumber = 0;

            for (int i = 1; i < expression.length; i++) {
                if(i % 2 == 0) {
                    try {
                        System.out.println(expression[i]);
                        new java.math.BigInteger(expression[i]);
                        //it is number
                        currentNumber = Integer.parseInt(expression[i]);
                    } catch (NumberFormatException e) {
                        // it is variable
                        try {
                            currentNumber = this.declarationInputParser.getVariableValue(expression[i]);
                        } catch (Exception ex) {
                            System.out.println("Such variable does not exist");
                        }
                    }

                    result = computeExpression(result, currentNumber, previousSign);
                } else {
                    // we have sign
                    System.out.println("is addition " + expression[i].equals("+"));
                    previousSign = expression[i];
                }
            }

            try {
                System.out.println("Result of computation" + result);
                this.declarationInputParser.assign(variableName, result);
//                result = 0;
            } catch (Exception e) {
                System.out.println("Such variable does not exist");
            }

        }
    }

    private int computeExpression(int currentResult, int value, String sign) {
        if (sign.equals("+")) {
            currentResult += value;
        } else {
            currentResult -= value;
        }

        return currentResult;
    }

    private boolean isDeclaration() {
        if (this.inputToParse.matches(declarationRegEx)) {
            return true;
        }
        return false;
    }

    private boolean isDeclarationWithAssigment() {
        if (this.inputToParse.matches(variableDeclarationWithAssignmentRegEx)) {
            return true;
        }
        return false;
    }

    private boolean isAssignment() {
        if (this.inputToParse.matches(variableAssignmentRegEx)) {
            return true;
        }
        return false;
    }

    private boolean isExistingVariableAssignmentToExistingVariable() {
        if (this.inputToParse.matches(variableAssignmentToExistingVariableRegEx)) {
            return true;
        }
        return false;
    }

    private boolean isDeclarationWithAssigmentToExistingVariable() {
        if (this.inputToParse.matches(variableDeclarationWithAssignmentToExistingVariableRegEx)) {
            return true;
        }
        return false;
    }

    private boolean isVariableCheck() {
        if (this.inputToParse.matches(variableValueCheck)) {
            return true;
        }
        return false;
    }

    private boolean isVariableAssigmentToExistingVariableWithEpression() {
        if (this.inputToParse.matches(variableAssigmentToExistingVariableWithEpression)) {
            return true;
        }
        return false;
    }

    public static String[] getSliceOfArray(String[] arr, int start, int end)
    {

        String[] slice = new String[end - start];

        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
        }

        return slice;
    }
}
