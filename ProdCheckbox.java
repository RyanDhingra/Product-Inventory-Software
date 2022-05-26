import javax.swing.JCheckBox;

public class ProdCheckbox extends JCheckBox {

    private Product associatedProd;

    public ProdCheckbox(String text, Product associatedProd) {

        super(text);
        this.associatedProd = associatedProd;
    }

    public Product getProd() {
        return this.associatedProd;
    }
}
