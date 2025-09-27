import javax.swing.*;
import java.awt.*;

public class ProductGUI {
    public void show() {
        JFrame frame = new JFrame("Product Management");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel sizeLabel = new JLabel("Size (S, M, L, XL):");
        JTextField sizeField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();

        JButton addButton = new JButton("Add Product");
        customizeButton(addButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String size = sizeField.getText().toUpperCase();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            Product product = new Product(name, size, quantity, price);
            JOptionPane.showMessageDialog(frame, "Product added successfully!");
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(sizeLabel);
        panel.add(sizeField);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(addButton);

        frame.add(panel);
        frame.setVisible(true);
    }
    static void customizeButton(JButton button) {
        button.setBackground(new Color(139, 69, 19)); // Beige
        button.setForeground(new Color(255, 255, 198));
        button.setFont(new Font("Cooper Black", Font.PLAIN, 14));
    }
}
