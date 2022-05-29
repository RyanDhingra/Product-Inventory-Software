import javax.swing.*;
import java.awt.*;
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
    
    private JPanel optionsPanel, menuTab, newProdPanel, inventoryPanel, scrollPanel, updateProdPanel;
    private JLabel title, imgLabel, prodLabel;
    private JFrame WIN, errorWindow;
    private ArrayList<Product> products;
    private JComboBox prodType, prodGender;
    private File currFile;
    private JTextField brandText, modelText, priceText, horizontalTensionText, verticalTensionText, gripSizeText, weightText, quantityText; 
    private String imgName;
    private JButton racquetDoneButton, backToOptions, backToEditProds, shoeDoneButton, backToUpdateProd, updatePriceButton, updateQuantityButton, updateSizeButton;
    private JTextArea descriptionText;
    private ArrayList<ProdCheckbox> checkBoxes, selectedBoxes;
    private ArrayList<JCheckBox> sizeCheckBoxes, updateSizeCheckBoxes;
    private Product prodToUpdate;
    private double newPrice;
    private Integer newQuantity;
    private ArrayList<Double> newSizes;

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

        panel.setVisible(false);
        optionsPanel = new JPanel();
        optionsPanel.setLayout(null);

        //Widgets
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
                sizes = sizes.substring(17, sizes.length());
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
            System.out.println(error.getMessage());
        }
    }

    //Updating products
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
            prodSelectBox.setBounds(0, yValue, 1000, 20);
            prodSelectBox.setFocusable(false);
            checkBoxes.add(prodSelectBox);
            scrollPanel.add(prodSelectBox);
            yValue += 20;
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

    @Override
    public void actionPerformed(ActionEvent click) {
        
        if (click.getActionCommand().equals("Edit Products")) {
            editProds();
        } else if (click.getSource() == backToOptions) {
            try {
                new Inventory(WIN, menuTab);
            } catch (FileNotFoundException error) {
                System.out.println(error.getMessage());
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
                errorWindow = new JFrame();
                errorWindow.setTitle("ERROR");
                errorWindow.setLocationRelativeTo(null);
                errorWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                errorWindow.setSize(250, 70);
                errorWindow.setLayout(null);
                errorWindow.setVisible(true);
                errorWindow.setResizable(false);

                JPanel errorPanel = new JPanel();
                errorPanel.setLayout(null);
                errorPanel.setSize(300, 200);
                errorPanel.setVisible(true);
                
                JLabel error = new JLabel("Please select one item to edit.");
                error.setBounds(10, 0, 300, 20);
                error.setForeground(Color.red);
                error.setFont(new Font("Arial", Font.BOLD, 15));

                errorPanel.add(error);
                errorWindow.add(errorPanel);
            }
        } else if (click.getActionCommand().equals("Delete Selected Products")) {
            products = deleteProds();
            updateProdFile();
            menuTab.setVisible(false);
            editProds();
        } else if (click.getSource() == prodType) {
            if (prodType.getSelectedItem().equals("Racquets")) {

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
                addImage.setBounds(630, 450, 100, 20);
                addImage.addActionListener(this);

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

            } else if (prodType.getSelectedItem().equals("Shoes")) {

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
                addImage.setBounds(630, 450, 100, 20);
                addImage.addActionListener(this);

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

            } else if (prodType.getSelectedItem().equals("Select Type...")) {

                newProdPanel.removeAll();
                newProdPanel.add(title);
                newProdPanel.add(prodType);
                newProdPanel.revalidate();
                newProdPanel.repaint();

            }

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
                        errorWindow = new JFrame();
                        errorWindow.setTitle("ERROR");
                        errorWindow.setLocationRelativeTo(null);
                        errorWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        errorWindow.setSize(250, 70);
                        errorWindow.setLayout(null);
                        errorWindow.setVisible(true);
                        errorWindow.setResizable(false);

                        JPanel errorPanel = new JPanel();
                        errorPanel.setLayout(null);
                        errorPanel.setSize(200, 200);
                        errorPanel.setVisible(true);
                        
                        JLabel error = new JLabel("Invalid file type.");
                        error.setBounds(60, 0, 150, 20); //Fix this
                        error.setForeground(Color.red);
                        error.setFont(new Font("Arial", Font.BOLD, 15));

                        errorPanel.add(error);
                        errorWindow.add(errorPanel);
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
                    }
                }

                products.add(new Shoe(Double.parseDouble(priceText.getText()), brandText.getText(), modelText.getText(), descriptionText.getText(), Integer.parseInt(quantityText.getText()), new File("Product Images\\" + imgName), selectedSizes, prodGender.getSelectedItem().toString()));
                updateProdFile();

                brandText.setText("");
                modelText.setText("");
                priceText.setText("");
                quantityText.setText("");
                descriptionText.setText("");
                selectedSizes.clear();
                prodGender.setSelectedItem("Select Gender...");
                currFile = null;

                for (JCheckBox sizeCheckBox: sizeCheckBoxes) {
                    sizeCheckBox.setSelected(false);
                }

                ImageIcon defaultImgIcon = new ImageIcon("Add Image.png");
                Image defaultImg = defaultImgIcon.getImage();
                Image resizedDefaultImg = defaultImg.getScaledInstance(350, 250, Image.SCALE_SMOOTH);
                ImageIcon resizedDefaultIcon = new ImageIcon(resizedDefaultImg);

                imgLabel.setIcon(resizedDefaultIcon);
                
            } catch (IOException error) {
                System.out.println(error.getMessage());
            }
        } else if (click.getActionCommand().equals("Update Price")) {
            updateProdPanel.removeAll();

            JLabel newPriceLabel = new JLabel("Enter new price:");
            newPriceLabel.setBounds(150, 120, 200, 20);

            JTextField newPriceText = new JTextField(20);
            newPriceText.setBounds(100, 160, 200, 20);

            updatePriceButton = new JButton("Done");
            updatePriceButton.setBounds(220, 220, 80, 20);
            updatePriceButton.addActionListener(this);

            backToUpdateProd = new JButton("Back");
            backToUpdateProd.setBounds(100, 220, 80, 20);
            backToUpdateProd.addActionListener(this);

            updateProdPanel.add(backToUpdateProd);
            updateProdPanel.add(updatePriceButton);
            updateProdPanel.add(newPriceLabel);
            updateProdPanel.add(newPriceText);
            updateProdPanel.add(updatePriceButton);
            updateProdPanel.add(title);
            updateProdPanel.add(prodLabel);

            updateProdPanel.revalidate();
            updateProdPanel.repaint();
        } else if (click.getActionCommand().equals("Update Quantity")) {
            updateProdPanel.removeAll();

            JLabel newQuantityLabel = new JLabel("Enter new quantity:");
            newQuantityLabel.setBounds(120, 120, 200, 20);

            JTextField newQuantityText = new JTextField(20);
            newQuantityText.setBounds(120, 160, 200, 20);

            updateQuantityButton = new JButton("Done");
            updateQuantityButton.setBounds(220, 220, 80, 20);
            updateQuantityButton.addActionListener(this);

            backToUpdateProd = new JButton("Back");
            backToUpdateProd.setBounds(100, 220, 80, 20);
            backToUpdateProd.addActionListener(this);
            
            updateProdPanel.add(backToUpdateProd);
            updateProdPanel.add(updatePriceButton);
            updateProdPanel.add(newQuantityLabel);
            updateProdPanel.add(newQuantityText);
            updateProdPanel.add(updateQuantityButton);
            updateProdPanel.add(title);
            updateProdPanel.add(prodLabel);

            updateProdPanel.revalidate();
            updateProdPanel.repaint();
        } else if (click.getActionCommand().equals("Update Sizes")) {
            updateProdPanel.removeAll();
            updateSizeCheckBoxes = new ArrayList<JCheckBox>();

            JLabel newSizeLabel = new JLabel("Select new sizes:");
            newSizeLabel.setBounds(145 , 120, 200, 20);

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
        } else if (click.getSource() == backToUpdateProd) {
            updateProdPanel.setVisible(false);
            updateProd(prodToUpdate);
        }
    }
}