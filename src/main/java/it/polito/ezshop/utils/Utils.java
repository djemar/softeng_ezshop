package it.polito.ezshop.utils;

import java.util.List;
import it.polito.ezshop.data.*;

public class Utils {
    public static boolean validateBarcode(String code) {
        if (code.length() >= 12 && code.length() <= 14) {
            int sum = 0;
            for (int i = code.length() - 1; i >= 0; i--) {
                if ((i + 1) % 2 == 0)
                    sum += Character.getNumericValue(code.charAt(i)) * 3;
                else
                    sum += Character.getNumericValue(code.charAt(i));
            }
            if (Math.round((sum + 5) / 10.0) * 10 - sum == Character
                    .getNumericValue(code.charAt(code.length() - 1)))
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

    public static boolean validateCreditCard(String number) {
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

    public static boolean containsProduct(final List<TicketEntry> list, final String productCode) {
    	//list.stream().anyMatch(x-> x.getBarCode().equals(productCode)
        return list.stream().filter(o -> o.getBarCode().equals(productCode)).findFirst()
                .isPresent();
    }

    public static TicketEntry getProductFromEntries(final List<TicketEntry> list,
            final String productCode) {
        return list.stream().filter(o -> o.getBarCode().equals(productCode)).findFirst().get();
    }

}

