package com.company;

public class HelperService {
    public static boolean isNumber(String element) {
        try {
            new java.math.BigInteger(element);
            Integer.parseInt(element);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int computeExpression(int currentResult, int value, String sign) {
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
