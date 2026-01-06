import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class CurrencyConverter {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String continueConversion = "yes";

        System.out.println("===== Currency Converter =====\n");

        while (continueConversion.equalsIgnoreCase("yes") || continueConversion.equalsIgnoreCase("y")) {
            try {
                System.out.print("Enter amount: ");
                double amount = sc.nextDouble();

                if (amount < 0) {
                    System.out.println("Amount cannot be negative. Please try again.\n");
                    continue;
                }

                System.out.print("Enter base currency (e.g., USD, INR, EUR, GBP, JPY, AUD, CAD, CHF): ");
                String from = sc.next().toUpperCase();

                System.out.print("Enter target currency (e.g., USD, INR, EUR, GBP, JPY, AUD, CAD, CHF): ");
                String to = sc.next().toUpperCase();

                String urlStr = "https://api.exchangerate-api.com/v4/latest/" + from;

                URL url = new URL(urlStr);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                br.close();

                // Parse JSON response
                String resultString = response.toString();

                // Check if API returned error
                if (resultString.contains("\"error\"") || !resultString.contains("\"" + to + "\"")) {
                    System.out.println("Error: Invalid currency code or API error. Please try again.\n");
                    continue;
                }

                // Extract rate value from response
                int rateIndex = resultString.indexOf("\"" + to + "\":");
                int valueStart = resultString.indexOf(":", rateIndex) + 1;
                int valueEnd = resultString.indexOf(",", valueStart);
                if (valueEnd == -1) {
                    valueEnd = resultString.indexOf("}", valueStart);
                }

                String rateStr = resultString.substring(valueStart, valueEnd).trim();
                double rate = Double.parseDouble(rateStr);
                double convertedAmount = amount * rate;

                // Format numbers with thousand separators
                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);

                System.out.println("\n✓ Converted Amount:");
                System.out.println("  " + nf.format(amount) + " " + from + " = " + nf.format(convertedAmount) + " " + to);
                System.out.println("  Exchange Rate: 1 " + from + " = " + String.format("%.4f", rate) + " " + to);

                System.out.print("\nConvert another amount? (yes/no): ");
                continueConversion = sc.next();
                System.out.println();

            } catch (java.net.MalformedURLException e) {
                System.out.println("Error: Invalid currency format. Please use standard currency codes like USD, EUR, INR, etc.\n");
            } catch (Exception e) {
                System.out.println("Error: Unable to fetch exchange rate. Please check your internet connection and try again.\n");
            }
        }

        System.out.println("Thank you for using Currency Converter!");
        sc.close();
    }
}
