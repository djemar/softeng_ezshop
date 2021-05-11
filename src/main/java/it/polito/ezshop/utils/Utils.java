package it.polito.ezshop.utils;

public class Utils {
    public static boolean validateBarcode(String code) {
        if (code.length() == 13) {
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                if ((i + 1) % 2 == 0)
                    sum += Character.getNumericValue(code.charAt(i)) * 3;
                else
                    sum += Character.getNumericValue(code.charAt(i));
            }
            if (Math.round((sum + 5) / 10.0) * 10 - sum == Character
                    .getNumericValue(code.charAt(12)))
                return true;
        }

        return false;
    }

    public static boolean isOnlyDigit(String string) {
        if (string.matches("-?\\d+(\\.\\d+)?"))
            return true;
        else
            return false;
    }
        // Luhn algorithm
        int nDigits = number.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = number.charAt(i) - '0';
            if (isSecond == true)
                d = d * 2;
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

}

