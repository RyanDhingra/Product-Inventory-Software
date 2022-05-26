import java.io.File;
import java.util.ArrayList;

public class Shoe extends Product {
    
    private ArrayList<Double> sizes;
    private String gender;

    public Shoe(double price, String brand, String model, String description, int quantity, File image, ArrayList<Double> sizes, String gender) {
        
        super(price, brand, model, description, quantity, image);
        this.sizes = sizes;
        this.gender = gender;
    }

    public ArrayList<Double> getSizes() {
        return this.sizes;
    }

    public String getGender() {
        return this.gender;
    }
}


