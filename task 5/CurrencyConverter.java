import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CurrencyConverter {
    
    private static Map<String, Double> exchangeRates = new HashMap<>();
    private static LocalDateTime lastUpdateTime;
    
    private static final String[] CURRENCIES = {
        "JPY", "USD", "EUR", "GBP", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD", "INR", "MXN"
    };
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n====================================");
        System.out.println("   CURRENCY CONVERTER - JPY ONLY");
        System.out.println("====================================\n");
        
        // Fetch initial rates
        fetchExchangeRates();
        
        while (true) {
            System.out.println("\n--- Options ---");
            System.out.println("1. Convert Amount");
            System.out.println("2. Refresh Exchange Rates");
            System.out.println("3. Exit");
            System.out.print("Choose option (1-3): ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    convertAmount(scanner);
                    break;
                case "2":
                    fetchExchangeRates();
                    break;
                case "3":
                    System.out.println("Thank you for using Currency Converter!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void fetchExchangeRates() {
        try {
            System.out.println("\nFetching exchange rates...");
            
            String urlString = "https://api.exchangerate-api.com/v4/latest/JPY";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                // Parse JSON manually (without external library)
                parseJSON(response.toString());
                
                lastUpdateTime = LocalDateTime.now();
                System.out.println("✓ Exchange rates updated successfully!");
                System.out.println("Last updated: " + lastUpdateTime.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ));
            } else {
                System.out.println("✗ Error: Failed to fetch rates (HTTP " + conn.getResponseCode() + ")");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private static void parseJSON(String json) {
        try {
            exchangeRates.clear();
            
            // Find the "rates" object
            int ratesStart = json.indexOf("\"rates\":{") + 9;
            int ratesEnd = json.lastIndexOf("}");
            String ratesStr = json.substring(ratesStart, ratesEnd);
            
            // Parse each currency rate
            String[] pairs = ratesStr.split(",");
            for (String pair : pairs) {
                int colonIndex = pair.indexOf(":");
                if (colonIndex > 0) {
                    String currency = pair.substring(0, colonIndex).trim().replace("\"", "");
                    String rateStr = pair.substring(colonIndex + 1).trim();
                    try {
                        double rate = Double.parseDouble(rateStr);
                        exchangeRates.put(currency, rate);
                    } catch (NumberFormatException e) {
                        // Skip invalid rates
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        }
    }
    
    private static void convertAmount(Scanner scanner) {
        try {
            System.out.print("\nEnter amount in JPY: ");
            String amountText = scanner.nextLine().trim();
            double amount = Double.parseDouble(amountText);
            
            System.out.println("\nAvailable currencies to convert to:");
            int index = 1;
            for (String currency : CURRENCIES) {
                if (!currency.equals("JPY")) {
                    System.out.printf("%2d. %s\n", index++, currency);
                }
            }
            
            System.out.print("Select target currency (1-11): ");
            String currencyChoice = scanner.nextLine().trim();
            int currencyIndex = Integer.parseInt(currencyChoice);
            
            if (currencyIndex < 1 || currencyIndex > 11) {
                System.out.println("Invalid currency selection.");
                return;
            }
            
            // Get the selected currency
            String targetCurrency = null;
            int count = 1;
            for (String currency : CURRENCIES) {
                if (!currency.equals("JPY")) {
                    if (count == currencyIndex) {
                        targetCurrency = currency;
                        break;
                    }
                    count++;
                }
            }
            
            if (targetCurrency != null && exchangeRates.containsKey(targetCurrency)) {
                double rate = exchangeRates.get(targetCurrency);
                double result = amount * rate;
                
                System.out.println("\n" + "=".repeat(50));
                System.out.printf("%.2f JPY = %.2f %s\n", amount, result, targetCurrency);
                System.out.printf("Exchange Rate: 1 JPY = %.6f %s\n", rate, targetCurrency);
                System.out.println("=".repeat(50));
            } else {
                System.out.println("Currency not found or rates not loaded.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}
