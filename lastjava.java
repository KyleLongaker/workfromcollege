import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the World Happiness - Corruption Dataset (2015-2020)");
        System.out.print("Enter a full pathname and filename for the input file: ");
        String filePath = scanner.nextLine();

        // Read data from CSV file
        CSVReader reader = new CSVReader();
        List<CountryData> dataList = reader.readCSV(filePath);

        if (dataList.isEmpty()) {
            System.out.println("No data found in the CSV file.");
            return;
        }

        System.out.println("Reading file\tDone");

        while (true) {
            System.out.print("Do you wish to (1) Search or (2) Sort by a column? (Enter 1 or 2): ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (choice == 1) {
                    searchAndPrint(dataList, scanner);
                } else if (choice == 2) {
                    sortAndPrint(dataList, scanner);
                } else {
                    System.out.println("Invalid input. Please enter 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

  private static void searchAndPrint(List<CountryData> dataList, Scanner scanner) {
    System.out.print("Enter the country name (leave blank for all): ");
    String country = scanner.nextLine().trim();
    System.out.print("Enter the continent (leave blank for all): ");
    String continent = scanner.nextLine().trim();
    System.out.print("Enter the year to search by (e.g., 2016, leave blank for all, 'all' for all years): ");
    String yearStr = scanner.nextLine().trim();

    Integer year = null;
    boolean allYears = false;

    if (!yearStr.isEmpty() && !yearStr.equalsIgnoreCase("all")) {
        try {
            year = Integer.parseInt(yearStr);
            if (year < 1800 || year > 2100) {
                System.out.println("Invalid year. Please enter a year between 1800 and 2100.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid year format. Please enter a numeric value.");
            return;
        }
    } else if (yearStr.equalsIgnoreCase("all")) {
        allYears = true;
    }

    final boolean finalAllYears = allYears;
    final Integer finalYear = year;

    List<CountryData> filteredList = dataList.stream()
        .filter(data -> country.isEmpty() || data.getCountry().equalsIgnoreCase(country))
        .filter(data -> continent.isEmpty() || data.getContinent().equalsIgnoreCase(continent))
       .filter(data -> finalAllYears || finalYear == null || data.getYear() == finalYear)
        .collect(Collectors.toList());

    if (filteredList.isEmpty()) {
        System.out.println("No data found for the given criteria.");
    } else {
        for (CountryData data : filteredList) {
            System.out.println(data);
        }
    }
}




private static void sortAndPrint(List<CountryData> dataList, Scanner scanner) {
    System.out.println("Select a column to sort by:");
    System.out.println("a. Country\nb. Year\nc. Continent\nd. Happiness Score\ne. GDP per Capita\nf. Family\ng. Health\nh. Freedom\ni. Generosity\nj. Government Trust\nk. CPI Score");
    System.out.print("Enter your choice (a-k): ");
    String choice = scanner.nextLine().trim().toLowerCase();
    System.out.print("Sort in (a)scending or (d)escending order? (a/d): ");
    String order = scanner.nextLine().trim().toLowerCase();

    Comparator<CountryData> comparator = null;

    switch (choice) {
        case "a": comparator = Comparator.comparing(CountryData::getCountry); break;
        case "b": comparator = Comparator.comparingInt(CountryData::getYear); break;
        case "c": comparator = Comparator.comparing(CountryData::getContinent); break;
        case "d": comparator = Comparator.comparingDouble(CountryData::getHappinessScore); break;
        case "e": comparator = Comparator.comparingDouble(CountryData::getGDPPerCapita); break;
        case "f": comparator = Comparator.comparingDouble(CountryData::getFamily); break;
        case "g": comparator = Comparator.comparingDouble(CountryData::getHealth); break;
        case "h": comparator = Comparator.comparingDouble(CountryData::getFreedom); break;
        case "i": comparator = Comparator.comparingDouble(CountryData::getGenerosity); break;
        case "j": comparator = Comparator.comparingDouble(CountryData::getGovernmentTrust); break;
        case "k": comparator = Comparator.comparingInt(CountryData::getCPIScore); break;
        default: 
            System.out.println("Invalid choice.");
            return;
    }

    if ("d".equals(order)) {
        comparator = comparator.reversed();
    }

    dataList.sort(comparator);
    for (CountryData data : dataList) {
        System.out.println(data);
    }
}


}
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class DataProcessor {
    // Sorts the dataList by country name in ascending order
    public void sortByCountry(List<CountryData> dataList) {
        Collections.sort(dataList, Comparator.comparing(CountryData::getCountry));
    }

    // Sorts the dataList by year in ascending order
    public void sortByYear(List<CountryData> dataList) {
        Collections.sort(dataList, Comparator.comparingInt(CountryData::getYear));
    }

    // Basic search method to find all entries for a specific country
    public List<CountryData> searchByCountry(String country, List<CountryData> dataList) {
        return dataList.stream()
                .filter(data -> data.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }
}
public class CountryData {
    private String country;
    private int year;
    private String continent;
    private double happinessScore;
    private double GDPPerCapita;
    private double family;
    private double health;
    private double freedom;
    private double generosity;
    private double governmentTrust;
    private int CPIScore;

    public CountryData(String country, int year, String continent, double happinessScore,
                   double gdpPerCapita, double family, double health, double freedom,
                   double generosity, double governmentTrust, int cpiScore) {
    // Initialize the object's properties here
    this.country = country;
    this.year = year;
    this.continent = continent;
    this.happinessScore = happinessScore;
    this.GDPPerCapita = GDPPerCapita;
    this.family = family;
    this.health = health;
    this.freedom = freedom;
    this.generosity = generosity;
    this.governmentTrust = governmentTrust;
    this.CPIScore = CPIScore;
}


    public String getCountry() {
        return country;
    }

    public int getYear() {
        return year;
    }

    public String getContinent() {
        return continent;
    }

    public double getHappinessScore() {
        return happinessScore;
    }

    public double getGDPPerCapita() {
        return GDPPerCapita;
    }

    public double getFamily() {
        return family;
    }

    public double getHealth() {
        return health;
    }

    public double getFreedom() {
        return freedom;
    }

    public double getGenerosity() {
        return generosity;
    }

    public double getGovernmentTrust() {
        return governmentTrust;
    }

    public int getCPIScore() {
        return CPIScore;
    }
// Override toString() for easy printing
    @Override
    public String toString() {
        return country + "\t" + year + "\t" + happinessScore + "\t" + GDPPerCapita + "\t" +
                family + "\t" + health + "\t" + freedom + "\t" + generosity + "\t" +
                governmentTrust + "\t" + CPIScore;
    }

}


    

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WorldHappinessCorruption {
    public static void main(String[] args) {
        List<CountryData> countryDataList = new ArrayList<>();

        // Load data from CSV file
        String csvFilePath = "C:/Users/public/WHC-15-20.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 11) {
                    int year = Integer.parseInt(data[1]); // Parse data[1] into an int
                    CountryData countryData = new CountryData(data[0], year, data[2],
                            Double.parseDouble(data[3]), Double.parseDouble(data[4]),
                            Double.parseDouble(data[5]), Double.parseDouble(data[6]),
                            Double.parseDouble(data[7]), Double.parseDouble(data[8]),
                            Double.parseDouble(data[9]), Integer.parseInt(data[10]));
                    countryDataList.add(countryData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Do you wish to (1) Search or (2) Sort by a column? (Enter 1 or 2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (choice == 1) {
                search(countryDataList, scanner);
            } else if (choice == 2) {
                sort(countryDataList, scanner);
            } else {
                System.out.println("Invalid input. Enter 1 or 2 only.");
            }
        }
    }

    private static void search(List<CountryData> countryDataList, Scanner scanner) {
        System.out.println("Search selected.");
        System.out.print("Enter the country name: ");
        String country = scanner.nextLine().trim();
        System.out.print("Enter the continent: ");
        String continent = scanner.nextLine().trim();
        System.out.print("Enter the year to search by (15-20 inclusive): ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Perform search based on user input
        for (CountryData data : countryDataList) {
            if ((country.isEmpty() || data.getCountry().equalsIgnoreCase(country))
                    && (continent.isEmpty() || data.getContinent().equalsIgnoreCase(continent))
                    && (year == 0 || (data.getYear() >= year && data.getYear() <= (year + 5)))) {
                // Print matching data
                System.out.println(data.getCountry() + "\t" + data.getYear() + "\t" + data.getContinent() + "\t" + data.getHappinessScore() +
                        "\t" + data.getGDPPerCapita() + "\t" + data.getFamily() + "\t" + data.getHealth() + "\t" + data.getFreedom() + "\t" +
                        data.getGenerosity() + "\t" + data.getGovernmentTrust() + "\t" + data.getCPIScore());
            }
        }
    }

    private static void sort(List<CountryData> countryDataList, Scanner scanner) {
        System.out.println("Sort selected.");
        System.out.println("Column to sort by:");
        System.out.println("a. country\nb. year\nc. continent\nd. happiness score\ne. GDP per capita\nf. family\ng. health\nh. freedom\ni. generosity\nj. government trust\nk. CPI score");
        System.out.print("Column to sort by (a-k): ");
        String column = scanner.nextLine().trim();

        // Perform sort based on user input
        switch (column) {
            case "a":
                countryDataList.sort((a, b) -> a.getCountry().compareTo(b.getCountry()));
                break;
            case "b":
                countryDataList.sort((a, b) -> Integer.compare(a.getYear(), b.getYear()));
                break;
            case "c":
                countryDataList.sort((a, b) -> a.getContinent().compareTo(b.getContinent()));
                break;
            case "d":
                countryDataList.sort((a, b) -> Double.compare(a.getHappinessScore(), b.getHappinessScore()));
                break;
            case "e":
                countryDataList.sort((a, b) -> Double.compare(a.getGDPPerCapita(), b.getGDPPerCapita()));
                break;
            case "f":
                countryDataList.sort((a, b) -> Double.compare(a.getFamily(), b.getFamily()));
                break;
            case "g":
                countryDataList.sort((a, b) -> Double.compare(a.getHealth(), b.getHealth()));
                break;
            case "h":
                countryDataList.sort((a, b) -> Double.compare(a.getFreedom(), b.getFreedom()));
                break;
            case "i":
                countryDataList.sort((a, b) -> Double.compare(a.getGenerosity(), b.getGenerosity()));
                break;
            case "j":
                countryDataList.sort((a, b) -> Double.compare(a.getGovernmentTrust(), b.getGovernmentTrust()));
                break;
            case "k":
                countryDataList.sort((a, b) -> Integer.compare(a.getCPIScore(), b.getCPIScore()));
                break;
            default:
                System.out.println("Invalid input. Please select a valid column.");
                return;
        }

        // Print sorted data
        for (CountryData data : countryDataList) {
            System.out.println(data.getCountry() + "\t" + data.getYear() + "\t" + data.getContinent() + "\t" + data.getHappinessScore() +
                    "\t" + data.getGDPPerCapita() + "\t" + data.getFamily() + "\t" + data.getHealth() + "\t" + data.getFreedom() + "\t" +
                    data.getGenerosity() + "\t" + data.getGovernmentTrust() + "\t" + data.getCPIScore());
        }
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.Collections;

public class CSVReader {
    public static List<CountryData> readCSV(String filePath) {
        List<CountryData> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // To skip the header row if present

            while ((line = br.readLine()) != null) {
                // Skip the header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length == 11) {
                    try {
                        String country = data[0];
                        int year = Integer.parseInt(data[1]);
                        String continent = data[2];
                        double happinessScore = Double.parseDouble(data[3]);
                        double gdpPerCapita = Double.parseDouble(data[4]);
                        double family = Double.parseDouble(data[5]);
                        double health = Double.parseDouble(data[6]);
                        double freedom = Double.parseDouble(data[7]);
                        double generosity = Double.parseDouble(data[8]);
                        double governmentTrust = Double.parseDouble(data[9]);
                        int cpiScore = Integer.parseInt(data[10]);
                        dataList.add(new CountryData(country, year, continent, happinessScore, gdpPerCapita, family, health, freedom, generosity, governmentTrust, cpiScore));
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing data from line: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found: " + filePath);
            return Collections.emptyList(); // Return an empty list
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return Collections.emptyList(); // Return an empty list
        }

        return dataList;
    }
}
