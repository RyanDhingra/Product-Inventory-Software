import java.io.File;

public class Racquet extends Product {
    
    private String gripSize, weight, verticalTension, horizontalTension;

    public Racquet(String brand, String model, double price, String horizontalTension, String verticalTension, String gripSize, String weight, String description, int quantity, File image) {
        
        super(price, brand, model, description, quantity, image);
        this.horizontalTension = horizontalTension;
        this.verticalTension = verticalTension;
        this.gripSize = gripSize;
        this.weight = weight;
    }

    public String getHorizontalTension() {
        return this.horizontalTension;
    }

    public String getVerticalTension() {
        return this.verticalTension;
    }

    public String getGripSize() {
        return this.gripSize;
    }

    public String getWeight() {
        return this.weight;
    }
}