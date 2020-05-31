package com.company;

public class Main {
    public static void main(String[] args) {
        UserInput input = new UserInput();
        InputParser parser = new InputParser();
        String userInput;

        while (true) {
            userInput = input.read();
            parser.parse(userInput);
        }
    }
}
