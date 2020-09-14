package com.company;

public class HelperService {
    public boolean isNumber(String element) {
        try {
            new java.math.BigInteger(element);
            //it is number
            Integer.parseInt(element);
            return true;
        } catch (NumberFormatException e) {
            // it is variable
            return false;
        }
    }

    public int computeExpression(int currentResult, int value, String sign) {
        if (sign.equals("+")) {
            currentResult += value;
        } else {
            currentResult -= value;
        }

        return currentResult;
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
