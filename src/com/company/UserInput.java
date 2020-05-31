package com.company;

import java.util.Scanner;

public class UserInput {
    private Scanner scan = new Scanner(System.in);

    public String read() {
        return this.scan.nextLine();
    }
}
