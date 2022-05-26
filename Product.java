import java.io.File;
import java.util.ArrayList;

public class Product {
    
    private double price;
    private String brand, model, description;
    private int quantity;
    private File image;

    public Product(double price, String brand, String model, String description, int quantity, File image) {

        this.brand = brand;
        this.price = price;
        this.model = model;
        this.description = description;
        this.quantity = quantity;
        this.image = image;
    }

    public String getBrand() {
        return this.brand;
    }

    public double getPrice() {
        return this.price;
    }

    public String getModel() {
        return this.model;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHorizontalTension() {
        return "";
    }

    public String getVerticalTension() {
        return "";
    }

    public String getGripSize() {
        return "";
    }

    public String getWeight() {
        return "";
    }

    public int getQuantity() {
        return this.quantity;
    }

    public File getImage() {
        return this.image;
    }

    public ArrayList<Double> getSizes() {
        return new ArrayList<Integer>();
    }

    public String getGender() {
        return "";
    }
}