package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Declarations {
    private String variableName;
    private HashMap<String, Integer> declarations = new HashMap<String, Integer>();

   public void add(String variableName) {
        this.declarations.put(variableName, null);

        System.out.println(this.declarations);
   }

   public void addAssignmentToVariable(String variableName, int value) {
        this.declarations.replace(variableName, value);

       System.out.println(this.declarations);
   }

//    public void addAssignmentToVariable(String variableName, String value) {
//        this.declarations.replace(variableName, value);
//
//        System.out.println(this.declarations);
//    }

    public HashMap<String, Integer> getDeclarations() {
        return this.declarations;
    }
}
