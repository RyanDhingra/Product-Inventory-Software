import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Inventory implements ActionListener {
    
    private JPanel optionsPanel, menuTab, newProdPanel, inventoryPanel, scrollPanel, updateProdPanel, viewProdPanel;
    private JLabel title, imgLabel, prodLabel;
    private JFrame WIN;
    private ErrorWindow errorWindow;
    private ArrayList<Product> products;
    private JComboBox prodType, prodGender, brandFilter, prodFilter, priceFilter, stockFilter;
    private File currFile;
    private JTextField brandText, modelText, priceText, horizontalTensionText, verticalTensionText, gripSizeText, weightText, quantityText, newPriceText, newQuantityText, searchBar; 
    private String imgName;
    private JButton racquetDoneButton, backToOptions, backToEditProds, shoeDoneButton, backToUpdateProd, updatePriceButton, updateQuantityButton, updateSizeButton, backToSearchProds;
    private JTextArea descriptionText;
    private ArrayList<ProdCheckbox> checkBoxes, selectedBoxes;
    private ArrayList<JCheckBox> sizeCheckBoxes, updateSizeCheckBoxes;
    private ArrayList<ProdButton> viewButtons;
    private Product prodToUpdate;
    
    public Inventory(JFrame WIN, JPanel panel) throws FileNotFoundException {

        //Window Configuration
        this.WIN = WIN;
        WIN.setSize(400, 300);
        WIN.setResizable(false);
        WIN.setLocationRelativeTo(null);
        WIN.setTitle("Login");
        WIN.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.products = new ArrayList<Product>();
        this.checkBoxes = new ArrayList<ProdCheckbox>();
        this.viewButtons = new ArrayList<ProdButton>();

        panel.setVisible(false);
        optionsPanel = new JPanel();
        optionsPanel.setLayout(null);

        //Options Menu
        JButton newProd = new JButton("Edit Products");
        newProd.setBounds(115, 65, 155, 50);
        newProd.addActionListener(this);

        JButton newPurchase = new JButton("Make Purchase");
        newPurchase.setBounds(115, 125, 155, 50);
        newPurchase.addActionListener(this);

        JButton searchProd = new JButton("Search Products");
        searchProd.setBounds(115, 185, 155, 50);
        searchProd.addActionListener(this);
        
        title = new JLabel("Options Menu");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setBounds(115, 0, 155, 50);
        
        optionsPanel.add(title);
        optionsPanel.add(newProd);
        optionsPanel.add(newPurchase);
        optionsPanel.add(searchProd);

        WIN.add(optionsPanel);
        
        //Inventory
        File inventoryFile = new File("Inventory.txt");
        Scanner inventoryScanner = new Scanner(inventoryFile);

        while (inventoryScanner.hasNextLine()) {

            String currLine = inventoryScanner.nextLine();

            if (currLine.equals("Racquet")) {

                String brand = inventoryScanner.nextLine();
                String model = inventoryScanner.nextLine();
                double price = inventoryScanner.nextDouble();
                String emptyLine = inventoryScanner.nextLine();
                String horizontalMaxTension = inventoryScanner.nextLine();
                String horizontalTensionNum = horizontalMaxTension.substring(2, horizontalMaxTension.length());
                String verticalMaxTension = inventoryScanner.nextLine();
                String verticalTensionNum = verticalMaxTension.substring(2, verticalMaxTension.length());
                String gripSize = inventoryScanner.nextLine();
                String weight =  inventoryScanner.nextLine();
                String description =  inventoryScanner.nextLine();
                int quantity = inventoryScanner.nextInt();
                emptyLine = inventoryScanner.nextLine();
                File image = new File("Product Images\\" + inventoryScanner.nextLine());
                
                products.add(new Racquet(brand, model, price, horizontalTensionNum, verticalTensionNum, gripSize, weight, description, quantity, image));
            
            } else if (currLine.equals("Shoe")) {
                
                String brand = inventoryScanner.nextLine();
                String model = inventoryScanner.nextLine();
                double price = inventoryScanner.nextDouble();
                String emptyLine = inventoryScanner.nextLine();
                String gender = inventoryScanner.nextLine();
                String sizes = inventoryScanner.nextLine();
                sizes = sizes.substring(16, sizes.length());
                Scanner sizeScanner = new Scanner(sizes);
                sizeScanner.useDelimiter(",");
                ArrayList<Double> sizeList = new ArrayList<Double>();
                
                while (sizeScanner.hasNext()) {
                    sizeList.add(Double.parseDouble(sizeScanner.next()));
                }

                String description =  inventoryScanner.nextLine();
                int quantity = inventoryScanner.nextInt();
                emptyLine = inventoryScanner.nextLine();
                File image = new File("Product Images\\" + inventoryScanner.nextLine());
                sizeScanner.close();

                products.add(new Shoe(price, brand, model, description, quantity, image, sizeList, gender));
                
            } //else if (currLine.equals("Shuttle"))
        }

        inventoryScanner.close();
    }

    //Updating products file
    public void updateProdFile() {

        try {
            FileWriter inventoryFile = new FileWriter("Inventory.txt");

            for (Product prod: products) {
                String prodType = prod.getClass().toString();
                prodType = prodType.substring(6, prodType.length());

                if (prodType.equals("Racquet")) {
                    inventoryFile.write(prodType + "\n");
                    inventoryFile.write(prod.getBrand() + "\n");
                    inventoryFile.write(prod.getModel() + "\n");
                    inventoryFile.write(prod.getPrice() + "\n");
                    inventoryFile.write("H:" + prod.getHorizontalTension() + "\n");
                    inventoryFile.write("V:" + prod.getVerticalTension() + "\n");
                    inventoryFile.write(prod.getGripSize() + "\n");
                    inventoryFile.write(prod.getWeight() + "\n");
                    inventoryFile.write(prod.getDescription() + "\n");
                    inventoryFile.write(prod.getQuantity() + "\n");
                    String[] imgNameArray = prod.getImage().toString().split("\\\\");
                    inventoryFile.write(imgNameArray[imgNameArray.length - 1] + "\n");
                    inventoryFile.write("\n");
                } else if (prodType.equals("Shoe")) {
                    inventoryFile.write(prodType + "\n");
                    inventoryFile.write(prod.getBrand() + "\n");
                    inventoryFile.write(prod.getModel() + "\n");
                    inventoryFile.write(prod.getPrice() + "\n");
                    inventoryFile.write(prod.getGender() + "\n");
                    String sizes = "Available Sizes:";
                    for (Double size: prod.getSizes()) {
                        int wholeSize = size.intValue();
                        if (size % wholeSize != 0) {
                            sizes += " " + size + ",";
                        } else {
                            sizes += " " + wholeSize + ",";
                        }
                    }
                    inventoryFile.write(sizes.substring(0, sizes.length() - 1) + "\n");
                    inventoryFile.write(prod.getDescription() + "\n");
                    inventoryFile.write(prod.getQuantity() + "\n");
                    String[] imgNameArray = prod.getImage().toString().split("\\\\");
                    inventoryFile.write(imgNameArray[imgNameArray.length - 1] + "\n");
                    inventoryFile.write("\n");
                }
            }

            inventoryFile.close();
        } catch (IOException error) {
            errorWindow = new ErrorWindow("Inventory file not found.");
        }
    }

    //Updating existing products
    public void updateProd(Product prod) {
        prodToUpdate = prod;

        if (selectedBoxes.size() != 0) {
            selectedBoxes.get(0).setSelected(false);
            selectedBoxes.clear();
        }
        
        menuTab.setVisible(false);

        updateProdPanel = new JPanel();
        updateProdPanel.setLayout(null);

        title = new JLabel("Update Product");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setBounds(110, 0, 200, 50);
        
        String prodType = prod.getClass().toString();
        prodType = prodType.substring(6, prodType.length());

        if (prodType.equals("Shoe")) {

            WIN.setSize(400, 400);

            prodLabel = new JLabel("Edit Shoe: " + prod.getBrand() + " " + prod.getModel());
            prodLabel.setBounds(20, 55, 380, 20);
            prodLabel.setFont(new Font("Verdana", Font.PLAIN, 15));

            JButton updatePrice = new JButton("Update Price");
            updatePrice.setBounds(120, 100, 150, 40);
            updatePrice.addActionListener(this);

            JButton updateQuantity = new JButton("Update Quantity");
            updateQuantity.setBounds(120, 160, 150, 40);
            updateQuantity.addActionListener(this);

            JButton updateSizes = new JButton("Update Sizes");
            updateSizes.setBounds(120, 220, 150, 40);
            updateSizes.addActionListener(this);  

            backToEditProds = new JButton("Back");
            backToEditProds.setBounds(145, 300, 100, 20);
            backToEditProds.addActionListener(this);

            updateProdPanel.add(updatePrice);
            updateProdPanel.add(updateQuantity);
            updateProdPanel.add(updateSizes);
            updateProdPanel.add(prodLabel);
            updateProdPanel.revalidate();
            updateProdPanel.repaint();

        } else if (prodType.equals("Racquet")) {

            WIN.setSize(400, 300);
            
            prodLabel = new JLabel("Edit Racquet: " + prod.getBrand() + " " + prod.getModel());
            prodLabel.setBounds(20, 55, 380, 20);
            prodLabel.setFont(new Font("Verdana", Font.PLAIN, 15));

            JButton updatePrice = new JButton("Update Price");
            updatePrice.setBounds(120, 100, 150, 40);
            updatePrice.addActionListener(this);

            JButton updateQuantity = new JButton("Update Quantity");
            updateQuantity.setBounds(120, 160, 150, 40);
            updateQuantity.addActionListener(this);

            backToEditProds = new JButton("Back");
            backToEditProds.setBounds(145, 220, 100, 20);
            backToEditProds.addActionListener(this);

            updateProdPanel.add(updatePrice);
            updateProdPanel.add(updateQuantity);
            updateProdPanel.add(prodLabel);
        }

        updateProdPanel.add(backToEditProds);
        updateProdPanel.add(title);
        WIN.add(updateProdPanel);
        updateProdPanel.revalidate();
        updateProdPanel.repaint();
    }

    //Adding new products screen
    public void newProd() {
        
        menuTab.setVisible(false);

        newProdPanel = new JPanel();
        newProdPanel.setLayout(null);

        title = new JLabel("New Product");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setBounds(415, 0, 200, 50);

        String[] prodTypes = {"Select Type...","Racquets","Shoes"};
        prodType = new JComboBox(prodTypes);
        prodType.setBounds(415, 130, 150, 20);
        prodType.addActionListener(this);

        backToEditProds = new JButton("Back");
        backToEditProds.setBounds(440, 55, 100, 20);
        backToEditProds.addActionListener(this);

        newProdPanel.add(backToEditProds);
        newProdPanel.add(prodType);
        newProdPanel.add(title);
        WIN.add(newProdPanel);
        
    }

    //Deleting selected products
    public ArrayList<Product> deleteProds() {

        ArrayList<Product> prodsToRemove = new ArrayList<Product>();

        for (ProdCheckbox checkBox: checkBoxes) {
            if (checkBox.isSelected()) {
                prodsToRemove.add(checkBox.getProd());
            }
        }

        for (Product prod: prodsToRemove) {
            if (products.contains(prod)) {
                products.remove(prod);
            }
        }

        return products;
    }

    //Editing products screen
    public void editProds() {
        checkBoxes.clear();
        
        optionsPanel.setVisible(false);
        WIN.setSize(1000, 800);
        WIN.setLayout(new BorderLayout());
        WIN.setLocationRelativeTo(null);

        menuTab = new JPanel();
        menuTab.setPreferredSize(new Dimension(1000, 800));
        menuTab.setLayout(null);
        menuTab.setBackground(Color.cyan);

        backToOptions = new JButton("Back");
        backToOptions.setBounds(35, 25, 200, 25);
        backToOptions.addActionListener(this);

        JButton newProd = new JButton("Add New Products");
        newProd.setBounds(275, 25, 200, 25);
        newProd.addActionListener(this);

        JButton deleteProd = new JButton("Delete Selected Products");
        deleteProd.setBounds(515, 25, 200, 25);
        deleteProd.addActionListener(this);

        JButton editSelectedProd = new JButton("Update Selected Item");
        editSelectedProd.setBounds(755, 25, 200, 25);
        editSelectedProd.addActionListener(this);

        menuTab.add(backToOptions);
        menuTab.add(newProd);
        menuTab.add(deleteProd);
        menuTab.add(editSelectedProd);

        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BorderLayout());
        inventoryPanel.setBounds(0, 75, 987, 690);

        scrollPanel = new JPanel();
        scrollPanel.setPreferredSize(new Dimension(1000, 5000));
        scrollPanel.setLayout(null);

        int yValue = 0;

        for (Product prod: products) {
            ProdCheckbox prodSelectBox = new ProdCheckbox(prod.getBrand() + " " + prod.getModel(), prod);
            prodSelectBox.setBounds(0, yValue, 1000, 25);
            prodSelectBox.setFocusable(false);
            checkBoxes.add(prodSelectBox);
            scrollPanel.add(prodSelectBox);
            yValue += 25;
        }
        
        inventoryPanel.add(BorderLayout.CENTER, new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        menuTab.add(inventoryPanel);
        WIN.add(menuTab);
        menuTab.revalidate();
        menuTab.repaint();
        inventoryPanel.revalidate();
        inventoryPanel.repaint();
        scrollPanel.revalidate();
        scrollPanel.repaint();
    } 

    public void searchProds() {
      
        optionsPanel.setVisible(false);
        WIN.setSize(1000, 800);
        WIN.setLayout(new BorderLayout());
        WIN.setLocationRelativeTo(null);

        menuTab = new JPanel();
        menuTab.setPreferredSize(new Dimension(1000, 800));
        menuTab.setLayout(null);
        menuTab.setBackground(Color.cyan);

        backToOptions = new JButton("Back");
        backToOptions.setBounds(238, 110, 150, 20);
        backToOptions.addActionListener(this);

        JLabel keywords = new JLabel("Enter Keywords:");
        keywords.setBounds(20, 10, 100, 20);

        JLabel filters = new JLabel("Filters:");
        filters.setBounds(70, 50, 50, 20);

        searchBar =  new JTextField();
        searchBar.setBounds(120, 10, 745, 20);

        String[] prodTypes = {"Select Product Type...", "Racquet", "Shoe"};
        prodFilter = new JComboBox(prodTypes);
        prodFilter.setBounds(120, 50, 160, 25);
        prodFilter.addActionListener(this);

        String[] stock = {"Select Availability...", "In Stock", "Out of Stock"};
        stockFilter = new JComboBox(stock);
        stockFilter.setBounds(705, 50, 160, 25);
        stockFilter.addActionListener(this);

        String[] priceRanges = {"Select Price Range...", "$0.00 - $49.99", "$50.00 - $99.99", "$100.00 - $149.99", "$150.00 - $199.99", "$200.00 - $249.99", "$250.00 - $299.99", "$300.00+"};
        priceFilter = new JComboBox(priceRanges);
        priceFilter.setBounds(510, 50, 160, 25);
        priceFilter.addActionListener(this);

        ArrayList<String> brands = new ArrayList<String>();
        brands.add("Select Brand...");

        for (Product prod: products) {
            if (!brands.contains(prod.getBrand())) {
                brands.add(prod.getBrand());
            }
        }

        String[] brandTypes = new String[brands.size()];

        int x = 0;
        for (String brand: brands) {
            brandTypes[x] = brand;
            x++;
        }

        brandFilter = new JComboBox(brandTypes);
        brandFilter.setBounds(315, 50, 160, 25);
        brandFilter.addActionListener(this);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(418, 110, 150, 20);
        searchButton.addActionListener(this);

        JButton clearFilters = new JButton("Clear Search");
        clearFilters.setBounds(598, 110, 150, 20);
        clearFilters.addActionListener(this);

        menuTab.add(clearFilters);
        menuTab.add(backToOptions);
        menuTab.add(searchButton);

        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BorderLayout());
        inventoryPanel.setBounds(0, 150, 987, 615);

        scrollPanel = new JPanel();
        scrollPanel.setPreferredSize(new Dimension(1000, 5000));
        scrollPanel.setLayout(null);

        JLabel clickToView =  new JLabel("Click to View Product:");
        clickToView.setBounds(17, 130, 200, 20);

        int yValue = 0;

        for (Product prod: products) {
            ProdButton viewProdButton = new ProdButton(prod.getBrand() + " " + prod.getModel(), prod);
            viewProdButton.addActionListener(this);
            viewProdButton.setBounds(0, yValue, 969, 25);
            viewProdButton.setFocusable(false);
            viewProdButton.setHorizontalAlignment(SwingConstants.LEFT);
            viewButtons.add(viewProdButton);
            scrollPanel.add(viewProdButton);
            yValue += 25;
        }
        
        inventoryPanel.add(BorderLayout.CENTER, new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        menuTab.add(keywords);
        menuTab.add(filters);
        menuTab.add(clickToView);
        menuTab.add(priceFilter);
        menuTab.add(brandFilter);
        menuTab.add(prodFilter);
        menuTab.add(stockFilter);
        menuTab.add(searchBar);
        menuTab.add(inventoryPanel);
        WIN.add(menuTab);
        menuTab.revalidate();
        menuTab.repaint();
        inventoryPanel.revalidate();
        inventoryPanel.repaint();
        scrollPanel.revalidate();
        scrollPanel.repaint();
    }

    public void searchFilters() {

        ArrayList<Product> prodTypeFiltered = new ArrayList<Product>();
        ArrayList<Product> brandTypeFiltered = new ArrayList<Product>();
        ArrayList<Product> priceRangeFiltered = new ArrayList<Product>();
        ArrayList<Product> stockOptionFiltered = new ArrayList<Product>();
        ArrayList<Product> searchTermFiltered = new ArrayList<Product>();

        String prodType = prodFilter.getSelectedItem().toString();
        String brandType = brandFilter.getSelectedItem().toString();
        String stockOption = stockFilter.getSelectedItem().toString();
        String priceRange = priceFilter.getSelectedItem().toString();
        String searchTerm = searchBar.getText();
        
        //Search Filters

        //Product Type Filter
        if (!prodType.equals("Select Product Type...")) {
            for (Product prod: products) {
                String currProdType = prod.getClass().toString();
                currProdType = currProdType.substring(6, currProdType.length());
                if (currProdType.equals(prodType)) {
                    prodTypeFiltered.add(prod);
                }
            }
        } else {
            for (Product prod: products) {
                prodTypeFiltered.add(prod);
            }
        }

        //Brand Type Filter
        if (!brandType.equals("Select Brand...")) {
            for (Product prod: prodTypeFiltered) {
                String currBrandType = prod.getBrand();
                if (currBrandType.equals(brandType)) {
                    brandTypeFiltered.add(prod);
                }
            }
        } else {
            for (Product prod: prodTypeFiltered) {
                brandTypeFiltered.add(prod);
            }
        }

        //Price Range Filter - fix intervals: e.g. 249.99 is not found
        if (priceRange.equals("$300.00+")) {
            double minPrice = 300.0;
            for (Product prod: brandTypeFiltered) {
                if (prod.getPrice() >= minPrice) {
                    priceRangeFiltered.add(prod);
                }
            }
        } else if (!priceRange.equals("Select Price Range...")) {
            priceRange = priceRange.replace("$", "");
            String[] prices = priceRange.split("-");
            double minPrice = Double.parseDouble(prices[0]);
            double maxPrice = Double.parseDouble(prices[1]);
            for (Product prod: brandTypeFiltered) {
                if (minPrice <= prod.getPrice() && maxPrice >= prod.getPrice()) {
                    priceRangeFiltered.add(prod);
                }
            }
        } else {
            for (Product prod: brandTypeFiltered) {
                priceRangeFiltered.add(prod);
            }
        }
        
        //Availability Filter
        if (stockOption.equals("Select Availability...")) {
            for (Product prod: priceRangeFiltered) {
                stockOptionFiltered.add(prod);
            }
        } else if (stockOption.equals("In Stock")) {
            for (Product prod: priceRangeFiltered) {
                if (prod.getQuantity() > 0) {
                    stockOptionFiltered.add(prod);
                }
            }
        } else if (stockOption.equals("Out of Stock")) {
            for (Product prod: priceRangeFiltered) {
                if (prod.getQuantity() == 0) {
                    stockOptionFiltered.add(prod);
                }
            }
        }

        //Searchbar
        ArrayList<String> prodNames = new ArrayList<String>();

        for (Product prod: stockOptionFiltered) {
            prodNames.add(prod.getBrand() + " " + prod.getModel());
        }

        if (searchTerm.equals("")) {
            for (Product prod: stockOptionFiltered) {
                searchTermFiltered.add(prod);
            }
        } else {
            for (String name: prodNames) {
                if (name.contains(searchTerm)) {
                    for (Product prod: products) {
                        String currName = prod.getBrand() + " " + prod.getModel();
                        if (currName.equals(name) && !searchTermFiltered.contains(prod)) {
                            searchTermFiltered.add(prod);
                        }
                    }
                }
            }
        }

        scrollPanel.removeAll();
        viewButtons.clear();
        int yValue = 0;

        for (Product prod: searchTermFiltered) {
            ProdButton viewProdButton = new ProdButton(prod.getBrand() + " " + prod.getModel(), prod);
            viewProdButton.addActionListener(this);
            viewProdButton.setBounds(0, yValue, 969, 25);
            viewProdButton.setFocusable(false);
            viewProdButton.setHorizontalAlignment(SwingConstants.LEFT);
            viewButtons.add(viewProdButton);
            scrollPanel.add(viewProdButton);
            yValue += 25;
        } 

        scrollPanel.revalidate();
        scrollPanel.repaint();
    }

    public void viewProd(Product prodToView) {

        menuTab.setVisible(false);
        String prodType = prodToView.getClass().toString();
        prodType = prodType.substring(6, prodType.length());

        viewProdPanel = new JPanel();
        viewProdPanel.setLayout(null);
            
        backToSearchProds = new JButton("Back");
        backToSearchProds.setBounds(460, 700, 80, 20);
        backToSearchProds.addActionListener(this);

        if (prodType.equals("Racquet")) {
            
            String title = prodToView.getBrand() + " " + prodToView.getModel();

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Roboto", Font.PLAIN, 40));
            titleLabel.setBounds(0, 30, 987, 50);

            JLabel modelLabel = new JLabel("Model:");
            modelLabel.setBounds(150, 230, 100, 20);
            JLabel model = new JLabel(prodToView.getModel());
            model.setBounds(150, 250, 300, 20);

            JLabel priceLabel = new JLabel("Price:");
            priceLabel.setBounds(150, 280, 100, 20);
            JLabel price = new JLabel(prodToView.getPrice() + "");
            price.setBounds(160, 300, 100, 20);
            JLabel dollarSign = new JLabel("$");
            dollarSign.setBounds(150, 300, 150, 20);

            JLabel quantityLabel = new JLabel("Quantity:");
            quantityLabel.setBounds(350, 280, 100, 20);
            JLabel quantity = new JLabel(prodToView.getQuantity() + "");
            quantity.setBounds(350, 300, 100, 20);

            JLabel maxTensionLabel = new JLabel("Maximum Tension:");
            maxTensionLabel.setBounds(150, 330, 150, 20);

            JLabel horizontalTensionLabel = new JLabel("H:");
            horizontalTensionLabel.setBounds(150, 350, 20, 20);
            JLabel horizontalTension = new JLabel(prodToView.getHorizontalTension());
            horizontalTension.setBounds(165, 350, 30, 20);
            
            JLabel verticalTensionLabel = new JLabel("V:");
            verticalTensionLabel.setBounds(240, 350, 20, 20);
            JLabel verticalTension = new JLabel(prodToView.getVerticalTension());
            verticalTension.setBounds(255, 350, 30, 20);

            JLabel gripSizeLabel = new JLabel("Grip Size:");
            gripSizeLabel.setBounds(150, 380, 150, 20);
            JLabel gripSize = new JLabel(prodToView.getGripSize());
            gripSize.setBounds(150, 400, 150, 20);

            JLabel weightLabel = new JLabel("Weight:");
            weightLabel.setBounds(150, 430, 150, 20);
            JLabel weight = new JLabel(prodToView.getWeight());
            weight.setBounds(150, 450, 150, 20);

            JLabel descriptionLabel = new JLabel("Description:");
            descriptionLabel.setBounds(150, 480, 300, 20);
            JTextArea description = new JTextArea(prodToView.getDescription());
            description.setBounds(150, 500, 700, 150);
            description.setEditable(false);
            
            ImageIcon prodImgIcn = new ImageIcon(prodToView.getImage().toString());
            Image prodImg = prodImgIcn.getImage();
            /*BufferedImage buffered = null;
            try {
                buffered = ImageIO.read(new File(prodToView.getImage().toString()));
                Graphics g = buffered.getGraphics();
                Graphics2D g2D = (Graphics2D) g;
                g2D.drawImage(buffered, 0, 0, null);
            } catch (IOException error) {
                System.out.println(error.getMessage());
            }*/
            Image prodImgResized = prodImg.getScaledInstance(350, 250, Image.SCALE_SMOOTH);
            //Image prodImgResized = (Image) buffered;
            ImageIcon image = new ImageIcon(prodImgResized);
            
            JLabel imgLabel = new JLabel("");
            imgLabel.setIcon(image);
            imgLabel.setBounds(505, 180, 350, 250);

            viewProdPanel.add(titleLabel);
            viewProdPanel.add(modelLabel);
            viewProdPanel.add(model);
            viewProdPanel.add(priceLabel);
            viewProdPanel.add(price);
            viewProdPanel.add(dollarSign);
            viewProdPanel.add(quantityLabel);
            viewProdPanel.add(quantity);
            viewProdPanel.add(maxTensionLabel);
            viewProdPanel.add(horizontalTensionLabel);
            viewProdPanel.add(horizontalTension);
            viewProdPanel.add(verticalTensionLabel);
            viewProdPanel.add(verticalTension);
            viewProdPanel.add(gripSizeLabel);
            viewProdPanel.add(gripSize);
            viewProdPanel.add(weightLabel);
            viewProdPanel.add(weight);
            viewProdPanel.add(descriptionLabel);
            viewProdPanel.add(description);
            viewProdPanel.add(imgLabel);

        } else if (prodType.equals("Shoe")) {
            System.out.println("shoe");
        }
        viewProdPanel.add(backToSearchProds);
        WIN.add(viewProdPanel);
    }

    public void newShoe() {
        
        newProdPanel.removeAll();
        sizeCheckBoxes = new ArrayList<JCheckBox>();

        JLabel brandLabel = new JLabel("Brand:");
        brandLabel.setBounds(150, 180, 100, 20);
        brandText = new JTextField(20);
        brandText.setBounds(150, 200, 300, 20);

        JLabel modelLabel = new JLabel("Model:");
        modelLabel.setBounds(150, 230, 100, 20);
        modelText = new JTextField(20);
        modelText.setBounds(150, 250, 300, 20);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(150, 280, 100, 20);
        priceText = new JTextField(20);
        priceText.setBounds(160, 300, 100, 20);
        JLabel dollarSign = new JLabel("$");
        dollarSign.setBounds(150, 300, 150, 20);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(350, 280, 100, 20);
        quantityText = new JTextField(20);
        quantityText.setBounds(350, 300, 100, 20);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(150, 330, 100, 20);
        String[] genders = {"Select Gender...","Mens","Womens","Unisex"};
        prodGender = new JComboBox(genders);
        prodGender.setBounds(150, 350, 150, 20);

        JLabel sizeLabel = new JLabel("Select Sizes:");
        sizeLabel.setBounds(150, 380, 150, 20);

        JCheckBox size7 = new JCheckBox("7");
        size7.setBounds(150, 410, 50, 20);
        sizeCheckBoxes.add(size7);
        
        JCheckBox size7_5 = new JCheckBox("7.5");
        size7_5.setBounds(150, 430, 50, 20);
        sizeCheckBoxes.add(size7_5);
        
        JCheckBox size8 = new JCheckBox("8");
        size8.setBounds(150, 450, 50, 20);
        sizeCheckBoxes.add(size8);
        
        JCheckBox size8_5 = new JCheckBox("8.5");
        size8_5.setBounds(250, 410, 50, 20);
        sizeCheckBoxes.add(size8_5);
        
        JCheckBox size9 = new JCheckBox("9");
        size9.setBounds(250, 430, 50, 20);
        sizeCheckBoxes.add(size9);
        
        JCheckBox size9_5 = new JCheckBox("9.5");
        size9_5.setBounds(250, 450, 50, 20);
        sizeCheckBoxes.add(size9_5);
        
        JCheckBox size10 = new JCheckBox("10");
        size10.setBounds(350, 410, 50, 20);
        sizeCheckBoxes.add(size10);
        
        JCheckBox size10_5 = new JCheckBox("10.5");
        size10_5.setBounds(350, 430, 50, 20);
        sizeCheckBoxes.add(size10_5);
        
        JCheckBox size11 = new JCheckBox("11");
        size11.setBounds(350, 450, 50, 20);
        sizeCheckBoxes.add(size11);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(150, 480, 300, 20);
        
        descriptionText = new JTextArea();
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setBounds(150, 500, 700, 150);
        descriptionText.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        descriptionText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keydown) {
                int maxChars = 648;
                int charCount = descriptionText.getText().length() + 1;

                if (charCount >= maxChars) {
                    descriptionText.setText(descriptionText.getText().substring(0, 647));
                }
            }
        });

        JButton addImage = new JButton("Add Image");
        addImage.setBounds(545, 450, 120, 20);
        addImage.addActionListener(this);

        JButton resetImage = new JButton("Reset Image");
        resetImage.setBounds(695, 450, 120, 20);
        resetImage.addActionListener(this);

        shoeDoneButton = new JButton("Done");
        shoeDoneButton.setBounds(460, 700, 80, 20);
        shoeDoneButton.addActionListener(this);
        
        ImageIcon defaultImgIcon = new ImageIcon("Add Image.png");
        Image defaultImg = defaultImgIcon.getImage();
        Image resizedDefaultImg = defaultImg.getScaledInstance(350, 250, Image.SCALE_SMOOTH);
        ImageIcon resizedDefaultIcon = new ImageIcon(resizedDefaultImg);
        
        imgLabel = new JLabel("");
        imgLabel.setIcon(resizedDefaultIcon);
        imgLabel.setBounds(505, 180, 350, 250);

        newProdPanel.add(brandText);
        newProdPanel.add(brandLabel);
        newProdPanel.add(modelText);
        newProdPanel.add(modelLabel);
        newProdPanel.add(priceText);
        newProdPanel.add(priceLabel);
        newProdPanel.add(dollarSign);
        newProdPanel.add(descriptionText);
        newProdPanel.add(descriptionLabel);
        newProdPanel.add(addImage);
        newProdPanel.add(resetImage);
        newProdPanel.add(shoeDoneButton);
        newProdPanel.add(imgLabel);
        newProdPanel.add(prodType);
        newProdPanel.add(title);
        newProdPanel.add(backToEditProds);
        newProdPanel.add(quantityLabel);
        newProdPanel.add(quantityText);
        newProdPanel.add(genderLabel);
        newProdPanel.add(prodGender);
        newProdPanel.add(sizeLabel);
        
        for (JCheckBox sizeCheckBox: sizeCheckBoxes) {
            newProdPanel.add(sizeCheckBox);
        }

        newProdPanel.revalidate();
        newProdPanel.repaint();
    }

    public void newRacquet() {
        
        newProdPanel.removeAll();

        JLabel brandLabel = new JLabel("Brand:");
        brandLabel.setBounds(150, 180, 100, 20);
        brandText = new JTextField(20);
        brandText.setBounds(150, 200, 300, 20);

        JLabel modelLabel = new JLabel("Model:");
        modelLabel.setBounds(150, 230, 100, 20);
        modelText = new JTextField(20);
        modelText.setBounds(150, 250, 300, 20);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(150, 280, 100, 20);
        priceText = new JTextField(20);
        priceText.setBounds(160, 300, 100, 20);
        JLabel dollarSign = new JLabel("$");
        dollarSign.setBounds(150, 300, 150, 20);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(350, 280, 100, 20);
        quantityText = new JTextField(20);
        quantityText.setBounds(350, 300, 100, 20);

        JLabel maxTensionLabel = new JLabel("Maximum Tension:");
        maxTensionLabel.setBounds(150, 330, 150, 20);

        JLabel horizontalTensionLabel = new JLabel("H:");
        horizontalTensionLabel.setBounds(150, 350, 20, 20);
        JLabel horizontalLBS = new JLabel("lbs");
        horizontalLBS.setBounds(195, 350, 20, 20);
        horizontalTensionText = new JTextField(20);
        horizontalTensionText.setBounds(165, 350, 30, 20);
        
        JLabel verticalTensionLabel = new JLabel("V:");
        verticalTensionLabel.setBounds(240, 350, 20, 20);
        JLabel verticalLBS = new JLabel("lbs");
        verticalLBS.setBounds(285, 350, 20, 20);
        verticalTensionText = new JTextField(20);
        verticalTensionText.setBounds(255, 350, 30, 20);

        JLabel gripSizeLabel = new JLabel("Grip Size:");
        gripSizeLabel.setBounds(150, 380, 150, 20);
        gripSizeText = new JTextField(20);
        gripSizeText.setBounds(150, 400, 150, 20);

        JLabel weightLabel = new JLabel("Weight:");
        weightLabel.setBounds(150, 430, 150, 20);
        weightText = new JTextField(20);
        weightText.setBounds(150, 450, 150, 20);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(150, 480, 300, 20);
        
        descriptionText = new JTextArea();
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setBounds(150, 500, 700, 150);
        descriptionText.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        descriptionText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keydown) {
                int maxChars = 648;
                int charCount = descriptionText.getText().length() + 1;

                if (charCount >= maxChars) {
                    descriptionText.setText(descriptionText.getText().substring(0, 647));
                }
            }
        });

        JButton addImage = new JButton("Add Image");
        addImage.setBounds(545, 450, 120, 20);
        addImage.addActionListener(this);

        JButton resetImage = new JButton("Reset Image");
        resetImage.setBounds(695, 450, 120, 20);
        resetImage.addActionListener(this);

        racquetDoneButton = new JButton("Done");
        racquetDoneButton.setBounds(460, 700, 80, 20);
        racquetDoneButton.addActionListener(this);
        
        ImageIcon defaultImgIcon = new ImageIcon("Add Image.png");
        Image defaultImg = defaultImgIcon.getImage();
        Image resizedDefaultImg = defaultImg.getScaledInstance(350, 250, Image.SCALE_SMOOTH);
        ImageIcon resizedDefaultIcon = new ImageIcon(resizedDefaultImg);
        
        imgLabel = new JLabel("");
        imgLabel.setIcon(resizedDefaultIcon);
        imgLabel.setBounds(505, 180, 350, 250);

        newProdPanel.add(brandText);
        newProdPanel.add(brandLabel);
        newProdPanel.add(modelText);
        newProdPanel.add(modelLabel);
        newProdPanel.add(priceText);
        newProdPanel.add(priceLabel);
        newProdPanel.add(dollarSign);
        newProdPanel.add(horizontalTensionText);
        newProdPanel.add(horizontalTensionLabel);
        newProdPanel.add(verticalTensionText);
        newProdPanel.add(verticalTensionLabel);
        newProdPanel.add(maxTensionLabel);
        newProdPanel.add(gripSizeText);
        newProdPanel.add(gripSizeLabel);
        newProdPanel.add(weightText);
        newProdPanel.add(weightLabel);
        newProdPanel.add(descriptionText);
        newProdPanel.add(descriptionLabel);
        newProdPanel.add(addImage);
        newProdPanel.add(resetImage);
        newProdPanel.add(racquetDoneButton);
        newProdPanel.add(imgLabel);
        newProdPanel.add(horizontalLBS);
        newProdPanel.add(verticalLBS);
        newProdPanel.add(quantityLabel);
        newProdPanel.add(quantityText);
        newProdPanel.add(prodType);
        newProdPanel.add(title);
        newProdPanel.add(backToEditProds);
        newProdPanel.revalidate();
        newProdPanel.repaint();
    }

    public void updatePrice() {
        updateProdPanel.removeAll();

        JLabel newPriceLabel = new JLabel("Enter new price:");
        newPriceLabel.setBounds(145, 120, 200, 20);

        newPriceText = new JTextField(20);
        newPriceText.setBounds(105, 160, 190, 20);

        JLabel dollarSign = new JLabel("$");
        dollarSign.setBounds(95, 160, 10, 20);

        updatePriceButton = new JButton("Done");
        updatePriceButton.setBounds(215, 220, 80, 20);
        updatePriceButton.addActionListener(this);

        backToUpdateProd = new JButton("Back");
        backToUpdateProd.setBounds(95, 220, 80, 20);
        backToUpdateProd.addActionListener(this);

        updateProdPanel.add(backToUpdateProd);
        updateProdPanel.add(updatePriceButton);
        updateProdPanel.add(newPriceLabel);
        updateProdPanel.add(newPriceText);
        updateProdPanel.add(updatePriceButton);
        updateProdPanel.add(title);
        updateProdPanel.add(prodLabel);
        updateProdPanel.add(dollarSign);

        updateProdPanel.revalidate();
        updateProdPanel.repaint();
    }

    public void updateQuantity() {
        updateProdPanel.removeAll();

        JLabel newQuantityLabel = new JLabel("Enter new quantity:");
        newQuantityLabel.setBounds(140, 120, 200, 20);

        newQuantityText = new JTextField(20);
        newQuantityText.setBounds(95, 160, 200, 20);

        updateQuantityButton = new JButton("Done");
        updateQuantityButton.setBounds(215, 220, 80, 20);
        updateQuantityButton.addActionListener(this);

        backToUpdateProd = new JButton("Back");
        backToUpdateProd.setBounds(95, 220, 80, 20);
        backToUpdateProd.addActionListener(this);
        
        updateProdPanel.add(backToUpdateProd);
        updateProdPanel.add(updateQuantityButton);
        updateProdPanel.add(newQuantityLabel);
        updateProdPanel.add(newQuantityText);
        updateProdPanel.add(title);
        updateProdPanel.add(prodLabel);

        updateProdPanel.revalidate();
        updateProdPanel.repaint();
    }

    public void updateSizes() {
        updateProdPanel.removeAll();
        updateSizeCheckBoxes = new ArrayList<JCheckBox>();

        JLabel newSizeLabel = new JLabel("Select new sizes:");
        newSizeLabel.setBounds(140, 120, 200, 20);

        JCheckBox size7 = new JCheckBox("7");
        size7.setBounds(70, 160, 50, 20);
        updateSizeCheckBoxes.add(size7);
        
        JCheckBox size7_5 = new JCheckBox("7.5");
        size7_5.setBounds(70, 180, 50, 20);
        updateSizeCheckBoxes.add(size7_5);
        
        JCheckBox size8 = new JCheckBox("8");
        size8.setBounds(70, 200, 50, 20);
        updateSizeCheckBoxes.add(size8);
        
        JCheckBox size8_5 = new JCheckBox("8.5");
        size8_5.setBounds(170, 160, 50, 20);
        updateSizeCheckBoxes.add(size8_5);
        
        JCheckBox size9 = new JCheckBox("9");
        size9.setBounds(170, 180, 50, 20);
        updateSizeCheckBoxes.add(size9);
        
        JCheckBox size9_5 = new JCheckBox("9.5");
        size9_5.setBounds(170, 200, 50, 20);
        updateSizeCheckBoxes.add(size9_5);
        
        JCheckBox size10 = new JCheckBox("10");
        size10.setBounds(270, 160, 50, 20);
        updateSizeCheckBoxes.add(size10);
        
        JCheckBox size10_5 = new JCheckBox("10.5");
        size10_5.setBounds(270, 180, 50, 20);
        updateSizeCheckBoxes.add(size10_5);
        
        JCheckBox size11 = new JCheckBox("11");
        size11.setBounds(270, 200, 50, 20);
        updateSizeCheckBoxes.add(size11);

        updateSizeButton = new JButton("Done");
        updateSizeButton.setBounds(215, 240, 80, 20);
        updateSizeButton.addActionListener(this);

        backToUpdateProd = new JButton("Back");
        backToUpdateProd.setBounds(95, 240, 80, 20);
        backToUpdateProd.addActionListener(this);

        for (JCheckBox sizeCheckBox: updateSizeCheckBoxes) {
            updateProdPanel.add(sizeCheckBox);
        }

        updateProdPanel.add(backToUpdateProd);
        updateProdPanel.add(updateSizeButton);
        updateProdPanel.add(newSizeLabel);
        updateProdPanel.add(title);
        updateProdPanel.add(prodLabel);

        updateProdPanel.revalidate();
        updateProdPanel.repaint();
    }

    @Override
    public void paint(Graphics g, Image img, int xPos, int yPos) {

        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(img, xPos, yPos, null);
        
    }

    @Override
    public void actionPerformed(ActionEvent click) {
        
        if (click.getActionCommand().equals("Edit Products")) {
            editProds();
        } else if (click.getActionCommand().equals("Search Products")) {
            searchProds();
        } else if (click.getSource() == backToOptions) {
            try {
                if (errorWindow != null) {
                    errorWindow.dispose();
                }
                new Inventory(WIN, menuTab);
            } catch (FileNotFoundException e) {
                if (errorWindow != null) {
                    errorWindow.dispose();
                }
                errorWindow = new ErrorWindow("Inventory file not found.");
            }
        } else if (click.getActionCommand().equals("Add New Products")) {
            newProd();
        } else if (click.getActionCommand().equals("Update Selected Item")) {
            if (errorWindow != null) {
                errorWindow.dispose();
            }

            selectedBoxes = new ArrayList<ProdCheckbox>();

            for (ProdCheckbox checkBox: checkBoxes) {
                if (checkBox.isSelected()) {
                    selectedBoxes.add(checkBox);
                }
            }

            if (selectedBoxes.size() == 1) {
                updateProd(selectedBoxes.get(0).getProd());
            } else {
                errorWindow = new ErrorWindow("Please select one item to edit.");
            }
        } else if (click.getActionCommand().equals("Delete Selected Products")) {
            products = deleteProds();
            updateProdFile();
            menuTab.setVisible(false);
            editProds();
        } else if (click.getSource() == prodType) {
            if (prodType.getSelectedItem().equals("Racquets")) {
                newRacquet();
            } else if (prodType.getSelectedItem().equals("Shoes")) {
                newShoe();
            } else if (prodType.getSelectedItem().equals("Select Type...")) {

                newProdPanel.removeAll();
                newProdPanel.add(title);
                newProdPanel.add(prodType);
                newProdPanel.add(backToEditProds);
                newProdPanel.revalidate();
                newProdPanel.repaint();
            }
        } else if (click.getActionCommand().equals("Reset Image")) {
            ImageIcon defaultImgIcon = new ImageIcon("Add Image.png");
            Image defaultImg = defaultImgIcon.getImage();
            Image resizedDefaultImg = defaultImg.getScaledInstance(350, 250, Image.SCALE_SMOOTH);
            ImageIcon resizedDefaultIcon = new ImageIcon(resizedDefaultImg);
            
            imgLabel.setIcon(resizedDefaultIcon);
            currFile = null;
        } else if (click.getActionCommand().equals("Add Image")) {
            if (errorWindow != null) {
                errorWindow.dispose();
            }

            JFileChooser imgUpload = new JFileChooser();
            int status = imgUpload.showOpenDialog(null);
            
            if (status == JFileChooser.APPROVE_OPTION) {
                
                String fileType = "";

                if (imgUpload != null) {
                    fileType = imgUpload.getSelectedFile().getAbsolutePath().substring(imgUpload.getSelectedFile().getAbsolutePath().length() - 3, imgUpload.getSelectedFile().getAbsolutePath().length());

                    if (fileType.equals("png") | fileType.equals("jpg")) {
                        currFile = new File(imgUpload.getSelectedFile().getAbsolutePath());
                        String[] imgPath = currFile.toString().split("\\\\");
                        imgName = imgPath[imgPath.length - 1];
                        
                        ImageIcon currImgIcon = new ImageIcon(currFile.toString());
                        Image currImg = currImgIcon.getImage();
                        Image resizedCurrImg = currImg.getScaledInstance(350, 250, Image.SCALE_SMOOTH);
                        ImageIcon resizedCurrImgIcon = new ImageIcon(resizedCurrImg);

                        imgLabel.setIcon(resizedCurrImgIcon);
                        imgLabel.repaint();
                    } else {
                        errorWindow = new ErrorWindow("Invalid file type.");
                    }
                }
            }         
        } else if (click.getSource() == racquetDoneButton) {
            try {
                File fileStorage = new File("C:\\Users\\ryand\\Downloads\\Inventory GUI\\Product Images\\" + imgName);
                
                if (currFile == null) {
                    imgName = "Add Image.png";
                    currFile = new File("Add Image.png");
                    fileStorage = new File("Product Images\\Add Image.png");
                }

                Files.copy(currFile.toPath(), fileStorage.toPath(), StandardCopyOption.REPLACE_EXISTING);
                products.add(new Racquet(brandText.getText(), modelText.getText(), Double.parseDouble(priceText.getText()), horizontalTensionText.getText() + "lbs", verticalTensionText.getText() + "lbs", gripSizeText.getText(), weightText.getText(), descriptionText.getText(), Integer.parseInt(quantityText.getText()), new File("Product Images\\" + imgName)));
                updateProdFile();

                brandText.setText("");
                modelText.setText("");
                priceText.setText("");
                quantityText.setText("");
                horizontalTensionText.setText("");
                verticalTensionText.setText("");
                gripSizeText.setText("");
                weightText.setText("");
                descriptionText.setText("");
                currFile = null;

                ImageIcon defaultImgIcon = new ImageIcon("Add Image.png");
                Image defaultImg = defaultImgIcon.getImage();
                Image resizedDefaultImg = defaultImg.getScaledInstance(350, 250, Image.SCALE_SMOOTH);
                ImageIcon resizedDefaultIcon = new ImageIcon(resizedDefaultImg);

                imgLabel.setIcon(resizedDefaultIcon);
                
            } catch (IOException error) {
                System.out.println(error.getMessage());
            } catch (NumberFormatException e) {
                if (errorWindow != null) {
                    errorWindow.dispose();
                }
                String[] invalidInput = e.getMessage().split(":");
                errorWindow = new ErrorWindow("Invalid input:" + invalidInput[1]);
            }
        } else if (click.getSource() == backToEditProds) {
            if (newProdPanel != null) {
                newProdPanel.setVisible(false);
                newProdPanel = null;
            } else if (updateProdPanel != null) {
                updateProdPanel.setVisible(false);
                updateProdPanel = null;
            }
            editProds();
        } else if (click.getSource() == shoeDoneButton) {
            if (errorWindow != null) {
                errorWindow = null; 
            }
            try {
                File fileStorage = new File("C:\\Users\\ryand\\Downloads\\Inventory GUI\\Product Images\\" + imgName);
                
                if (currFile == null) {
                    imgName = "Add Image.png";
                    currFile = new File("Add Image.png");
                    fileStorage = new File("Product Images\\Add Image.png");
                }

                Files.copy(currFile.toPath(), fileStorage.toPath(), StandardCopyOption.REPLACE_EXISTING);

                ArrayList<Double> selectedSizes = new ArrayList<Double>();

                for (JCheckBox sizeCheckBox: sizeCheckBoxes) {
                    if (sizeCheckBox.isSelected()) {
                        selectedSizes.add(Double.parseDouble(sizeCheckBox.getText()));
                        sizeCheckBox.setSelected(false);
                    }
                }

                if (selectedSizes.isEmpty()) {
                    errorWindow = new ErrorWindow("Select sizes.");
                } else {
                    products.add(new Shoe(Double.parseDouble(priceText.getText()), brandText.getText(), modelText.getText(), descriptionText.getText(), Integer.parseInt(quantityText.getText()), new File("Product Images\\" + imgName), selectedSizes, prodGender.getSelectedItem().toString()));
                    updateProdFile();

                    brandText.setText("");
                    modelText.setText("");
                    priceText.setText("");
                    quantityText.setText("");
                    descriptionText.setText("");
                    prodGender.setSelectedItem("Select Gender...");
                    currFile = null;

                    ImageIcon defaultImgIcon = new ImageIcon("Add Image.png");
                    Image defaultImg = defaultImgIcon.getImage();
                    Image resizedDefaultImg = defaultImg.getScaledInstance(350, 250, Image.SCALE_SMOOTH);
                    ImageIcon resizedDefaultIcon = new ImageIcon(resizedDefaultImg);

                    imgLabel.setIcon(resizedDefaultIcon);
                }
            } catch (IOException error) {
                System.out.println(error.getMessage());
            } catch (NumberFormatException e) {
                if (errorWindow != null) {
                    errorWindow.dispose();
                }

                if (e.getMessage().equals("empty String")) {
                    errorWindow = new ErrorWindow("Please fill in all areas.");
                } else {
                    String[] invalidInput = e.getMessage().split(":");
                    errorWindow = new ErrorWindow("Invalid input:" + invalidInput[1]);
                }
            }
        } else if (click.getActionCommand().equals("Update Price")) {
            updatePrice();
        } else if (click.getActionCommand().equals("Update Quantity")) {
            updateQuantity();
        } else if (click.getActionCommand().equals("Update Sizes")) {
            updateSizes();
        } else if (click.getSource() == backToUpdateProd) {
            updateProdPanel.setVisible(false);
            updateProd(prodToUpdate);
        } else if (click.getSource() == updatePriceButton) {
            prodToUpdate.updatePrice(Double.parseDouble(newPriceText.getText()));
            newPriceText.setText("");
            updateProdFile();
        } else if (click.getSource() == updateQuantityButton) {
            prodToUpdate.updateQuantity(Integer.parseInt(newQuantityText.getText()));
            newQuantityText.setText("");
            updateProdFile();
        } else if (click.getSource() == updateSizeButton) {
            if (errorWindow != null) {
                errorWindow = null;
            }

            ArrayList<Double> selectedSizes = new ArrayList<Double>();

            for (JCheckBox checkBox: updateSizeCheckBoxes) {
                if (checkBox.isSelected()) {
                    selectedSizes.add(Double.parseDouble(checkBox.getText()));
                    checkBox.setSelected(false);   
                }
            }

            if (selectedSizes.isEmpty()) {
                errorWindow = new ErrorWindow("Select sizes.");
            } else {
                prodToUpdate.updateSizes(selectedSizes);
                updateProdFile();
            }
        } else if (click.getActionCommand().equals("Search")) {
            searchFilters();
        } else if (click.getActionCommand().equals("Clear Search")) {
            
            prodFilter.setSelectedIndex(0);
            brandFilter.setSelectedIndex(0);
            priceFilter.setSelectedIndex(0);
            stockFilter.setSelectedIndex(0);
            searchBar.setText("");
        } else if (click.getSource() == backToSearchProds) {
            viewProdPanel.setVisible(false);
            searchProds();
        }

        for (ProdButton button: viewButtons) {
            if (click.getSource() == button) {
                viewProd(button.getProd());
            }
        }
    }
}