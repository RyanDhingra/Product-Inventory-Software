import javax.swing.JButton;

public class ProdButton extends JButton {
    
    private Product associatedProd;

    public ProdButton(String text, Product associatedProd) {

        super(text);
        this.associatedProd = associatedProd;
    }

    public Product getProd() {
        return this.associatedProd;
    }
}
