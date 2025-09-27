import javax.swing.*;
import java.awt.*;

public class CustomerMenuGUI {
    private JFrame frame;
    private Inventory inventory = new Inventory();
    private java.util.List<Product> cart = new java.util.ArrayList<>();
    private GenericListManager<Customer> customerManager = new GenericListManager<>(CoffeeShop.CUSTOMERS_FILE);
    private Delivery delivery;

    public void displayMenu(JFrame parentFrame) {
        frame = new JFrame("Customer Menu");
        frame.setSize(450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 1));
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Customer Menu", JLabel.CENTER);
        JButton viewProductsButton = new JButton("View Products");
        JButton addToCartButton = new JButton("Add to Cart");
        JButton viewCartButton = new JButton("View Cart");
        JButton addDeliveryButton = new JButton("Add Delivery Details");
        JButton viewDeliveryButton = new JButton("View Delivery Details");
        JButton checkoutButton = new JButton("Checkout");
        JButton exitButton = new JButton("Exit to Main Menu");

        customizeButton(viewProductsButton);
        customizeButton(addToCartButton);
        customizeButton(viewCartButton);
        customizeButton(addDeliveryButton);
        customizeButton(viewDeliveryButton);
        customizeButton(checkoutButton);
        customizeButton(exitButton);


        frame.add(titleLabel);
        frame.add(viewProductsButton);
        frame.add(addToCartButton);
        frame.add(viewCartButton);
        frame.add(addDeliveryButton);
        frame.add(viewDeliveryButton);
        frame.add(checkoutButton);
        frame.add(exitButton);

        frame.setVisible(true);

        viewProductsButton.addActionListener(e -> inventory.checkInventoryGUI(frame));

        addToCartButton.addActionListener(e -> addToCart());

        viewCartButton.addActionListener(e -> viewCart());

        addDeliveryButton.addActionListener(e -> addDeliveryDetails());

        viewDeliveryButton.addActionListener(e -> viewDeliveryDetails());

        checkoutButton.addActionListener(e -> checkout());

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

    private void addToCart() {
        while (true) {
            try {
                JTextField productNameField = new JTextField();
                JTextField sizeField = new JTextField();
                JTextField quantityField = new JTextField();

                Object[] inputFields = {
                        "Product Name:", productNameField,
                        "Size (S, M, L, XL):", sizeField,
                        "Quantity:", quantityField
                };

                int option = JOptionPane.showConfirmDialog(frame, inputFields, "Add to Cart", JOptionPane.OK_CANCEL_OPTION);
                if (option != JOptionPane.OK_OPTION) {
                    return;
                }

                String name = productNameField.getText().trim();
                String size = sizeField.getText().trim().toUpperCase();
                String quantityInput = quantityField.getText().trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Product Name is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (!name.matches("^[a-zA-Z ]+$")) {
                    JOptionPane.showMessageDialog(frame, "Invalid Product Name! Only alphabets and spaces are allowed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (size.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Size is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (!size.matches("(?i)S|M|L|XL")) {
                    JOptionPane.showMessageDialog(frame, "Invalid size! Please enter S, M, L, or XL.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (quantityInput.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Quantity is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityInput);
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(frame, "Quantity must be a positive integer!", "Input Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Quantity! Please enter a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                Product product = inventory.findProduct(name, size);
                if (product != null && product.getQuantity() >= quantity) {
                    boolean productExists = false;
                    for (Product cartProduct : cart) {
                        if (cartProduct.getName().equalsIgnoreCase(name) && cartProduct.getSize().equalsIgnoreCase(size)) {
                            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
                            productExists = true;
                            break;
                        }
                    }
                    if (!productExists) {
                        cart.add(new Product(name, size, quantity, product.getPrice()));
                    }
                    product.setQuantity(product.getQuantity() - quantity);
                    inventory.saveInventory();
                    JOptionPane.showMessageDialog(frame, "Product added to cart!");
                    break;
                } else {
                    JOptionPane.showMessageDialog(frame, "Product not available or insufficient quantity.");
                    continue;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewCart() {
        StringBuilder cartDetails = new StringBuilder();
        double total = 0;

        if (cart.isEmpty()) {
            cartDetails.append("No products found in cart.");
        } else {
            for (Product product : cart) {
                cartDetails.append(product).append("\n");
                total += product.getPrice() * product.getQuantity();
            }
            cartDetails.append("\nTotal Amount: ").append(total);
        }

        JOptionPane.showMessageDialog(frame, cartDetails.toString(), "Cart Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addDeliveryDetails() {
        while (true) {
            JTextField nameField = new JTextField();
            JTextField addressField = new JTextField();
            JTextField cityField = new JTextField();
            JTextField phoneField = new JTextField();

            Object[] inputFields = {
                    "Recipient Name:", nameField,
                    "Delivery Address:", addressField,
                    "City:", cityField,
                    "Phone Number:", phoneField
            };

            int option = JOptionPane.showConfirmDialog(frame, inputFields, "Add Delivery Details", JOptionPane.OK_CANCEL_OPTION);
            if (option != JOptionPane.OK_OPTION) {
                break;
            }

            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String city = cityField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || !name.matches("^[a-zA-Z ]+$")) {
                JOptionPane.showMessageDialog(frame, "Invalid name! Only alphabets and spaces are allowed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (address.isEmpty() || !address.matches("^[a-zA-Z0-9, ]+$")) {
                JOptionPane.showMessageDialog(frame, "Invalid address! Only alphabets, numbers, spaces, and commas are allowed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (city.isEmpty() || !city.matches("^[a-zA-Z ]+$")) {
                JOptionPane.showMessageDialog(frame, "Invalid city! Only alphabets and spaces are allowed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (phone.isEmpty() || !phone.matches("^\\d{11}$")) {
                JOptionPane.showMessageDialog(frame, "Invalid phone number! It must be exactly 11 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            delivery = new Delivery(name, address, city, phone);
            JOptionPane.showMessageDialog(frame, "Delivery details added successfully!");
            break;
        }
    }
    private void viewDeliveryDetails() {
        if (delivery == null) {
            JOptionPane.showMessageDialog(frame, "No delivery details available.", "Delivery Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, delivery.toString(), "Delivery Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No products found in cart.");
            return;
        }

        int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to proceed with the checkout?", "Checkout", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            if (delivery == null) {
                JOptionPane.showMessageDialog(frame, "Delivery details not provided. Provide details first.");
                addDeliveryDetails();
            }
            viewDeliveryDetails();
            JOptionPane.showMessageDialog(frame, "Thank you for your purchase! Your order will be processed.");
            cart.clear();
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{11}");
    }
}
