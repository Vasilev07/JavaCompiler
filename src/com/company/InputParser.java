package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    private String inputToParse;
    private DeclarationInputParser declarationInputParser = new DeclarationInputParser();
    private String declarationRegEx ="\\w+\\s\\w+;";
    private String variableDeclarationRegEx = "\\s+";
    private String variableAssignmentRegEx = "\\w+\\s\\w+\\s=\\s\\d+;";
    private Pattern pattern;
    private Matcher matcher;

    public void parse(String input) {
        this.inputToParse = input;
        String[] words = this.inputToParse.split("\\s+");

        if (isDeclaration()) {
            System.out.println("we have declaration here");

//            String declarationType = words[0];
            String variableName = words[1].substring(0, words[1].length() - 1);
            this.declarationInputParser.declare(variableName);
        } else if (isDeclarationWithAssigment()) {
            System.out.println("we have assignment here");

            String declarationType = words[0];
            String variableName = words[1];
            int variableValue = Integer.parseInt(words[3].substring(0, words[3].length() - 1));
//            System.out.println(words[3].substring(0, words[3].length() - 1));
            this.declarationInputParser.declare(variableName);
            this.declarationInputParser.assign(variableName, variableValue);
        }
    }

    private boolean isDeclaration() {
        if (this.inputToParse.matches(declarationRegEx)) {
            return true;
        }
        return false;
    }

    private boolean isDeclarationWithAssigment() {
        if (this.inputToParse.matches(variableAssignmentRegEx)) {
            return true;
        }
        return false;
    }


}
