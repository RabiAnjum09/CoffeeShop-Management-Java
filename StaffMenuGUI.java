import javax.swing.*;
import java.awt.*;

public class StaffMenuGUI {
    private Inventory inventory = new Inventory();
    private GenericListManager<Staff> staffManager = new GenericListManager<>(CoffeeShop.STAFF_FILE);

    public void displayMenu(JFrame parentFrame) {

        parentFrame.setVisible(false);

        JFrame frame = new JFrame("Staff Menu");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 1));
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Staff Menu", JLabel.CENTER);
        JButton addProductsButton = new JButton("Add Products");
        JButton checkInventoryButton = new JButton("Check Inventory");
        JButton modifyInventoryButton = new JButton("Modify Inventory");
        JButton exitButton = new JButton("Exit to Main Menu");

        customizeButton(addProductsButton);
        customizeButton(checkInventoryButton);
        customizeButton(modifyInventoryButton);
        customizeButton(exitButton);

        frame.add(titleLabel);
        frame.add(addProductsButton);
        frame.add(checkInventoryButton);
        frame.add(modifyInventoryButton);
        frame.add(exitButton);

        frame.setVisible(true);

        addProductsButton.addActionListener(e -> {
            while (true) {
                try {
                    JTextField nameField = new JTextField();
                    JTextField sizeField = new JTextField();
                    JTextField quantityField = new JTextField();
                    JTextField priceField = new JTextField();

                    Object[] inputFields = {
                            "Product Name:", nameField,
                            "Size (S, M, L, XL):", sizeField,
                            "Quantity:", quantityField,
                            "Price:", priceField
                    };

                    int option = JOptionPane.showConfirmDialog(
                            frame,
                            inputFields,
                            "Add Product",
                            JOptionPane.OK_CANCEL_OPTION
                    );

                    if (option != JOptionPane.OK_OPTION) {
                        return;
                    }

                    String name = nameField.getText().trim();
                    String size = sizeField.getText().trim();
                    String quantityInput = quantityField.getText().trim();
                    String priceInput = priceField.getText().trim();

                    if (name.isEmpty() || size.isEmpty() || quantityInput.isEmpty() || priceInput.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    if (!name.matches("^[a-zA-Z ]+$")) {
                        JOptionPane.showMessageDialog(frame, "Invalid product name! Only alphabets and spaces are allowed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    if (!size.matches("(?i)S|M|L|XL")) {
                        JOptionPane.showMessageDialog(frame, "Invalid size! Please enter S, M, L, or XL.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    int quantity;
                    double price;

                    try {
                        quantity = Integer.parseInt(quantityInput);
                        if (quantity <= 0) {
                            JOptionPane.showMessageDialog(frame, "Quantity must be a positive integer!", "Input Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid quantity! Please enter a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    try {
                        price = Double.parseDouble(priceInput);
                        if (price <= 0) {
                            JOptionPane.showMessageDialog(frame, "Price must be a positive number!", "Input Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid price! Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    inventory.getProducts().add(new Product(name, size, quantity, price));
                    inventory.saveInventory();
                    JOptionPane.showMessageDialog(frame, "Product added successfully!");
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        checkInventoryButton.addActionListener(e -> {
            try {
                inventory.loadInventory();
                JTextArea inventoryArea = new JTextArea();
                inventoryArea.setEditable(false);

                for (Product product : inventory.getProducts()) {
                    inventoryArea.append(product.toString() + "\n");
                }

                JScrollPane scrollPane = new JScrollPane(inventoryArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));
                JOptionPane.showMessageDialog(frame, scrollPane, "Inventory Details", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error checking inventory: " + ex.getMessage());
            }
        });

        modifyInventoryButton.addActionListener(e -> {
            try {
                inventory.loadInventory();
                String[] productOptions = inventory.getProducts().stream()
                        .map(product -> product.getName() + " (Size: " + product.getSize() + ")")
                        .toArray(String[]::new);

                if (productOptions.length == 0) {
                    JOptionPane.showMessageDialog(frame, "No products available to modify.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String selectedProduct = (String) JOptionPane.showInputDialog(
                        frame,
                        "Select a product to modify:",
                        "Modify Inventory",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        productOptions,
                        productOptions[0]
                );

                if (selectedProduct == null) {
                    return;
                }

                Product product = inventory.getProducts().stream()
                        .filter(p -> (p.getName() + " (Size: " + p.getSize() + ")").equals(selectedProduct))
                        .findFirst()
                        .orElse(null);

                if (product == null) {
                    JOptionPane.showMessageDialog(frame, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JTextField quantityField = new JTextField(String.valueOf(product.getQuantity()));
                JTextField priceField = new JTextField(String.valueOf(product.getPrice()));

                Object[] inputFields = {
                        "Quantity:", quantityField,
                        "Price:", priceField
                };

                while (true) {
                    int option = JOptionPane.showConfirmDialog(
                            frame,
                            inputFields,
                            "Modify Product: " + product.getName() + " (Size: " + product.getSize() + ")",
                            JOptionPane.OK_CANCEL_OPTION
                    );

                    if (option == JOptionPane.CANCEL_OPTION) {
                        return;
                    }

                    try {
                        String quantityInput = quantityField.getText().trim();
                        String priceInput = priceField.getText().trim();

                        if (quantityInput.isEmpty() || priceInput.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "Both fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        int newQuantity = Integer.parseInt(quantityInput);
                        if (newQuantity <= 0) {
                            JOptionPane.showMessageDialog(frame, "Quantity must be a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        double newPrice = Double.parseDouble(priceInput);
                        if (newPrice <= 0) {
                            JOptionPane.showMessageDialog(frame, "Price must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        product.setQuantity(newQuantity);
                        product.setPrice(newPrice);

                        inventory.saveInventory();
                        JOptionPane.showMessageDialog(frame, "Product modified successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        return;

                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(frame, "Invalid input: Please enter valid numeric values for quantity and price.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error modifying inventory: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        exitButton.addActionListener(e -> {
            frame.dispose();
            parentFrame.setVisible(true);
        });
    }
    static void customizeButton(JButton button) {
        button.setForeground(new Color(139, 69, 19));
        button.setBackground(new Color(255, 255, 198));
        button.setFont(new Font("Cooper Black", Font.PLAIN, 14));
    }
}
