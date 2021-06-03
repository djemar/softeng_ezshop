package it.polito.ezshop.data;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Utils {

	public static boolean validateBarcode(String code) {
		if (code.length() >= 12 && code.length() <= 14 && isOnlyDigit(code)) {
			int sum = 0;
			boolean x3 = true;
			for (int i = code.length() - 2; i >= 0; i--) {
				if (x3) {
					sum += Character.getNumericValue(code.charAt(i)) * 3;
					x3 = false;
				} else {
					sum += Character.getNumericValue(code.charAt(i));
					x3 = true;
				}
			}
			if (Math.ceil((sum + 5) / 10.0) * 10 - sum == Character
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
		if (nDigits < 13 || nDigits > 19)
			return false;
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
		return list.stream().anyMatch(x -> x.getBarCode().equals(productCode));
	}

	public static boolean containsCustomer(final List<Customer> list, final String name) {
		return list.stream().anyMatch(x -> x.getCustomerName().equals(name));
	}

	public static TicketEntry getProductFromEntries(final List<TicketEntry> list,
			final String productCode) {
		return list.stream().filter(o -> o.getBarCode().equals(productCode)).findFirst().get();
	}

	public static boolean fromFile(String creditcard, double total, String file) {
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
			List<String> lines = in.lines().collect(toList());
			in.close();
			return lines.stream().anyMatch(row -> {
				if (!row.startsWith("#")) {
					String[] cells = row.split(";");
					return cells[0].equalsIgnoreCase(creditcard)
							&& Double.parseDouble(cells[1]) >= total;
				}
				return false;
			});
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean updateFile(String file, String creditcard, double total) {
		boolean found = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			String newline, content = "";

			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) {
					content = content + line + "\n";
					continue;
				}
				newline = line;
				String l[] = line.split(";");
				if (l[0].equalsIgnoreCase(creditcard)) {
					found = true;
					total = Double.parseDouble(l[1]) - total;
					if (total < 0) {
						br.close();
						return false;
					}
					newline = new String(l[0] + ";" + String.valueOf(total));
				}
				content = content + newline + "\n";
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (!found)
			return false;
		return true;
	}

}

