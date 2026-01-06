# Currency Converter - Java Desktop Application

A JavaFX-based currency converter application that converts from Japanese Yen (JPY) to various other currencies with real-time exchange rates.

## Features

- **Convert from JPY** - Fixed base currency is Japanese Yen
- **Real-time Exchange Rates** - Fetches from exchangerate-api.com
- **Multiple Target Currencies** - Convert to USD, EUR, GBP, AUD, CAD, CHF, CNY, SEK, NZD, INR, MXN
- **Modern GUI** - Clean, user-friendly JavaFX interface
- **Status Updates** - Real-time feedback on data loading and errors
- **Refresh Button** - Manually refresh exchange rates

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Setup

1. **Install Maven** (if not already installed):
   - Download from: https://maven.apache.org/download.cgi
   - Add Maven to your PATH

2. **Install JavaFX SDK** (Optional, Maven will download dependencies):
   - The pom.xml will automatically download JavaFX libraries

3. **Navigate to project directory**:
   ```
   cd d:\currency
   ```

## Running the Application

### Option 1: Using Maven
```bash
mvn clean javafx:run
```

### Option 2: Build and Run as JAR
```bash
mvn clean package
java -jar target/currency-converter-1.0-SNAPSHOT.jar
```

### Option 3: Compile and Run Directly (if dependencies are available)
```bash
javac --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml CurrencyConverter.java
java --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml CurrencyConverter
```

## Usage

1. Launch the application
2. Enter the amount in JPY you want to convert
3. Select the target currency from the dropdown
4. The converted amount appears automatically
5. Click "Refresh Rates" to update exchange rates manually

## API Used

- **exchangerate-api.com** - Free API for real-time exchange rates (no API key required)

## File Structure

- `CurrencyConverter.java` - Main application class with GUI and logic
- `pom.xml` - Maven configuration with dependencies

## Dependencies

- **JavaFX 21** - For building the desktop GUI
- **Gson 2.10.1** - For JSON parsing

---

Enjoy converting currencies!
