package it.polito.ezshop.utils;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.ezshop.data.*;


public class Utils {
    public static boolean validateBarcode(String code) {
		if (code.length() >= 12 && code.length() <= 14) {
			int sum = 0;
			boolean x3 = true;
			for (int i = code.length()-2; i >= 0; i--) {
				if (x3) {
					sum += Character.getNumericValue(code.charAt(i)) * 3;
					x3 = false;
				}	
				else {
					sum += Character.getNumericValue(code.charAt(i));
					x3 = true;
				}	
			}
			if (Math.ceil((sum + 5) / 10.0) * 10 - sum == Character.getNumericValue(code.charAt(code.length()-1)))
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
        return list.stream().anyMatch(x-> x.getBarCode().equals(productCode));
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
				System.out.print(row);
				String[] cells = row.split(";");
				return cells[0].equalsIgnoreCase(creditcard) && Double.parseDouble(cells[1]) >= total;

			}
			return false;
		});
	}

	public static void updateFile(String file, String creditcard, double total){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line, newline,content = null;
			while((line = br.readLine())!= null){
				if(line.startsWith("#")) {
					content=content+line+"\n";
					continue;
				}
				newline = line;
				String l[] = line.split(";");
				if(l[0].equalsIgnoreCase(creditcard)) {
					total = Double.parseDouble(l[1]) - total;
					newline= new String(l[0] + ";"+ String.valueOf(total) );
				}
				content=content+ newline+ "\n";
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

