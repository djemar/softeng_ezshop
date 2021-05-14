package it.polito.ezshop.utils;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.data.*;


public class Utils {
    public static boolean validateBarcode(String code) {
        return true;
        // if (code.length() >= 12 && code.length() <= 14) {
        // int sum = 0;
        // for (int i = code.length() - 2; i >= 0; i--) {
        // if ((i + 1) % 2 == 0)
        // sum += Character.getNumericValue(code.charAt(i)) * 3;
        // else
        // sum += Character.getNumericValue(code.charAt(i));
        // }
        // if (Math.round((sum + 5) / 10.0) * 10 - sum == Character
        // .getNumericValue(code.charAt(code.length() - 1)))
        // return true;
        // }

        // return false;

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
        // list.stream().anyMatch(x-> x.getBarCode().equals(productCode)
        return list.stream().filter(o -> o.getBarCode().equals(productCode)).findFirst()
                .isPresent();
    }

    public static TicketEntry getProductFromEntries(final List<TicketEntry> list,
            final String productCode) {
        return list.stream().filter(o -> o.getBarCode().equals(productCode)).findFirst().get();
    }
	private static List<String> readData(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			return in.lines().collect(toList());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	public static boolean fromFile(String creditcard, double total, String file) {
		List<String> lines = Utils.readData(file);
		return lines.stream().anyMatch(row -> {
			if(!row.startsWith("#")) {
				String[] cells = row.split(";");
				return cells[0].equalsIgnoreCase(creditcard) && Double.parseDouble(cells[1]) >= total;

			}
			return false;
		});
	
	}
}

