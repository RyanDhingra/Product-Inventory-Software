import java.io.File;

public class Shuttle extends Product {
    
    private String grade, material; 

    public Shuttle(double price, String brand, String model, String description, int quantity, File image, String grade, String material) {

        super(price, brand, model, description, quantity, image);
        this.grade = grade;
        this.material = material;
    }

    public String getGrade() {
        return this.grade;
    }

    public String getMaterial() {
        return this.material;
    }
}
