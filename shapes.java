import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // Prompt the user to enter dimensions and weight for the shapes
            double length = getPositiveDouble("Enter the length: ");
            double width = getPositiveDouble("Enter the width: ");
            double height = getPositiveDouble("Enter the height: ");
            double weight = getPositiveDouble("Enter the weight: ");
            // Create instances of the Box, Cone, Cylinder, and Sphere classes
            Box box = new Box(length, width, height, weight);
            Cone cone = new Cone(length, width, height, weight);
            Cylinder cylinder = new Cylinder(height, length, width, weight);

            Sphere sphere = new Sphere(Math.max(length, width), weight);
            System.out.println();
            // Print the details of the created shapes
            System.out.println("Shapes Created:");
            System.out.println(box);
            System.out.println();
            System.out.println(cone);
            System.out.println();
            System.out.println(cylinder);
            System.out.println();
            System.out.println(sphere);

            // Calculate and print volumes
           // Calculate and print volumes and densities side by side
            System.out.println("\n");
            System.out.printf("Box volume: %.2f cu ft\t\tBox density: %.2f lbs./cu ft%n", box.calculateVolume(), box.calculateDensity());
            System.out.printf("Cone volume: %.2f cu ft\t\tCone density: %.2f lbs./cu ft%n", cone.calculateVolume(), cone.calculateDensity());
            System.out.printf("Cylinder volume: %.2f cu ft\t\tCylinder density: %.2f lbs./cu ft%n", cylinder.calculateVolume(), cylinder.calculateDensity());
            System.out.printf("Sphere volume: %.2f cu ft\t\tSphere density: %.2f lbs./cu ft%n", sphere.calculateVolume(), sphere.calculateDensity());

            // Calculate and print waste
            System.out.println("\nAn object container would need to be:");
            calculateAndPrintWaste(box, "Box");
            calculateAndPrintWaste(cone, "Cone");
            calculateAndPrintWaste(cylinder, "Cylinder");
            calculateAndPrintWaste(sphere, "Sphere");
        } catch (IllegalArgumentException e) {
             // Handle exceptions if the user enters invalid dimensions or weight
            System.out.println("Error: " + e.getMessage());
        }
    }
    // Helper method to get a positive double value from the user
    private static double getPositiveDouble(String prompt) {
        Scanner scanner = new Scanner(System.in);
        double value;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextDouble()) {
                System.out.print("Please enter a valid number: ");
                scanner.next();
            }
            value = scanner.nextDouble();
        } while (value <= 0);
        return value;
    }
    // Helper method to calculate and print waste for a given shape
    private static void calculateAndPrintWaste(Shape shape, String shapeName) {
        double containerVolume = shape.calculateBestFit();
        double waste = shape.calculateWaste();
        System.out.printf("%.2f cu ft for a %s (%.2f%% waste)%n", containerVolume, shapeName, waste);
    }

}
// Box class implementation
class Box extends Shape {
    // Declare instance variables to store the dimensions of the box
    protected double length;
    protected double width;
    protected double height;

    // Constructor for creating a Box object with specified dimensions and weight
    public Box(double length, double width, double height, double weight) {
        // Call the constructor of the superclass (Shape) with the provided weight
        super(weight);
        
        // Check if any of the dimensions (length, width, or height) is not positive, and throw an exception if so
        if (length <= 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        
        // Initialize the instance variables with the provided values
        this.length = length;
        this.width = width;
        this.height = height;
    }

    // Getter methods to retrieve the dimensions of the box
    public double getLength() {
        return length;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    // Override the calculateVolume() method to calculate the volume of the box
    @Override
    public double calculateVolume() {
        return length * width * height;
    }

    // Override the calculateDensity() method to calculate the density of the box
    @Override
    public double calculateDensity() {
        // Calculate the density by dividing the weight by the volume
        return weight / calculateVolume();
    }

    // Override the calculateBestFit() method to calculate the volume of the best-fitting container for the box
    @Override
    public double calculateBestFit() {
        // For a box, the best fit is the volume itself, as no extra space is needed
        return calculateVolume();
    }

    // Override the calculateWaste() method to calculate the wasted space as a percentage (which is always 0 for a box)
    @Override
    public double calculateWaste() {
        // No waste for a box
        return 0;
    }

    // Override the toString() method to provide a custom string representation of the Box object
    @Override
    public String toString() {
        return String.format("A Box of length %.2f, width %.2f, height %.2f, and weight %.2f lb. was created.",
                length, width, height, weight);
    }
}
// The abstract class for all shapes
abstract class Shape {
    // Declare an instance variable to store the weight of the shape
    protected double weight;

    // Constructor for creating a Shape object with a specified weight
    public Shape(double weight) {
        // Check if the provided weight is not positive, and throw an exception if so
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        
        // Initialize the instance variable with the provided weight
        this.weight = weight;
    }

    // Getter method to retrieve the weight of the shape
    public double getWeight() {
        return weight;
    }

    // Abstract methods for shape calculations
    // These methods are declared as abstract and will be implemented by concrete shape classes
    public abstract double calculateVolume();   // Calculate the volume of the shape
    public abstract double calculateDensity();  // Calculate the density of the shape
    public abstract double calculateBestFit(); // Calculate the volume of the best-fitting container for the shape
    public abstract double calculateWaste();    // Calculate the wasted space as a percentage

    // Abstract classes can define method signatures (like above) but not the method implementation
    // Subclasses (e.g., Box, Cone, Cylinder, Sphere) must provide concrete implementations for these methods

    // Abstract classes can also have concrete methods (with implementations) if needed
}
import java.util.Arrays;     
// Define a class named Cone that extends the Shape class   
class Cone extends Shape {
    // Declare instance variables to store the height and diameter of the cone
    protected double height;
    protected double diameter;
     // Constructor for creating a Cone object with specified dimensions and weight
    public Cone(double length, double width, double height, double weight) {
        // Call the constructor of the superclass (Shape) with the provided weight
        super(weight);
        // Create an array to store the dimensions (length, width, height) and sort it in ascending order
        double[] dimensions = {length, width, height};
        Arrays.sort(dimensions);
        // Set the height of the cone to the maximum value from the sorted dimensions
        this.height = dimensions[2]; // Maximum value
        // Set the diameter of the cone to the second largest value from the sorted dimensions
        this.diameter = dimensions[1]; // Second largest value
        // Check if the height or diameter is not positive, and throw an exception if so
        if (this.height <= 0 || this.diameter <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }
    // Override the calculateVolume() method to calculate the volume of the cone
    @Override
    public double calculateVolume() {
        double radius = diameter / 2;
        return (Math.PI * Math.pow(radius, 2) * height) / 3;
    }
    // Override the calculateDensity() method to calculate the density of the cone
    @Override
    public double calculateDensity() {
        // Calculate the density by dividing the weight by the volume
        return weight / calculateVolume();
    }
    // Override the calculateBestFit() method to calculate the volume of the best-fitting container for the cone
    @Override
    public double calculateBestFit() {
    double radius = diameter / 2;
    // Calculate the volume of a cylinder that can best fit the cone
    // (same base area and height as the cone)
    return Math.PI * Math.pow(radius, 2) * height;
    }
    // Override the calculateWaste() method to calculate the wasted space as a percentage
     @Override
    public double calculateWaste() {
        double containerVolume = calculateBestFit();
        // Calculate the waste as a percentage using the formula: ((containerVolume - coneVolume) / containerVolume) * 100
        double waste = 100 * ((containerVolume - calculateVolume()) / containerVolume);
        return waste;
    }
     // Override the toString() method to provide a custom string representation of the Cone object
    @Override
    public String toString() {
        return String.format("A Cone of height %.2f, diameter %.2f, and weight %.2f lb. was created.",
                this.height, this.diameter, this.weight);
    }
}
import java.util.Arrays; 
// Define a class named Cylinder that extends the Shape class
class Cylinder extends Shape {
     // Declare instance variables to store the height and diameter of the cylinder
    protected double height;
    protected double diameter;
    // Constructor for creating a Cylinder object with specified dimensions and weight
    public Cylinder(double height, double length, double width, double weight) {
        // Call the constructor of the superclass (Shape) with the provided weight
        super(weight);
        // Ensure height is the maximum value among height, length, and width
        this.height = Math.max(height, Math.max(length, width));
        // Ensure diameter is the second largest value among height, length, and width
        double[] dimensions = {height, length, width};
        Arrays.sort(dimensions);
        this.diameter = dimensions[1];
         // Check if the height or diameter is not positive, and throw an exception if so
        if (this.height <= 0 || this.diameter <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }
     // Override the calculateVolume() method to calculate the volume of the cylinder
      @Override
    public double calculateVolume() {
        double radius = diameter / 2;
        return Math.PI * Math.pow(radius, 2) * height;
    }
     // Override the calculateDensity() method to calculate the density of the cylinder
    @Override
    public double calculateDensity() {
        return weight / calculateVolume();
    }
     // Override the calculateBestFit() method to calculate the volume of the best-fitting container for the cylinder
    @Override
    public double calculateBestFit() {
        return Math.PI * Math.pow(diameter / 2, 2) * height;
    }
     // Override the calculateWaste() method to calculate the wasted space as a percentage
    @Override
    public double calculateWaste() {
        double containerVolume = calculateBestFit();
        return 100 * (containerVolume - calculateVolume()) / containerVolume;
    }
     // Override the toString() method to provide a custom string representation of the Cylinder object
    @Override
    public String toString() {
        return String.format("A Cylinder of height %.2f, diameter %.2f, and weight %.2f lb. was created.",
                this.height, this.diameter, this.weight);
    }
}
import java.util.Arrays;
// Define a class named Sphere that extends the Shape class
class Sphere extends Shape {
    protected double diameter;
    // Declare an instance variable to store the diameter of the sphere
    // Constructor for creating a Sphere object with specified diameter and weight
    public Sphere(double diameter, double weight) {
    // Call the constructor of the superclass (Shape) with the provided weight
    super(weight);
    // Initialize the diameter of the sphere
    this.diameter = diameter;
    // Check if the diameter or weight is not positive, and throw an exception if so
    if (this.diameter <= 0 || this.weight <= 0) {
        throw new IllegalArgumentException("Dimensions and weight must be positive");
    }
    }
    // Getter method to retrieve the diameter of the sphere
    public double getDiameter() {
        return diameter;
    }
    // Override the calculateVolume() method to calculate the volume of the sphere
    @Override
    public double calculateVolume() {
        double radius = diameter / 2;
        // Use the formula for the volume of a sphere: (4/3) * π * r^3
        return (4.0 / 3.0) * Math.PI * Math.pow(radius, 3);
    }
    // Override the calculateDensity() method to calculate the density of the sphere
    @Override
    public double calculateDensity() {
        // Calculate the density by dividing the weight by the volume
        return weight / calculateVolume();
    }
    // Override the calculateBestFit() method to calculate the volume of the best-fitting container for the sphere
    @Override
    public double calculateBestFit() {
        double radius = diameter / 2;
        // Calculate the volume of a sphere with the same diameter as the given sphere
        return (4.0 / 3.0) * Math.PI * Math.pow(radius, 3);
    }
    // Override the calculateWaste() method to calculate the wasted space as a percentage
    @Override
    public double calculateWaste() {
        double containerVolume = calculateBestFit();
         // Calculate the waste as a percentage using the formula: ((containerVolume - sphereVolume) / containerVolume) * 100
        return 100 * (containerVolume - calculateVolume()) / containerVolume;
    }
    // Override the toString() method to provide a custom string representation of the Sphere object
    @Override
    public String toString() {
        return String.format("A Sphere of diameter %.2f and weight %.2f lb. was created.",
                this.diameter, this.weight);
    }
}
