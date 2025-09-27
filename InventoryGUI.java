import javax.swing.*;
import java.awt.*;

public class InventoryGUI {
    private Inventory inventory;

    public InventoryGUI() {
        inventory = new Inventory();
    }

    public void show() {
        JFrame frame = new JFrame("Inventory Management");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        JButton viewButton = new JButton("View Inventory");
        JButton modifyButton = new JButton("Modify Inventory");
        JButton addProductButton = new JButton("Add Product");

        viewButton.addActionListener(e -> {
            inventoryArea.setText("");
            inventory.checkInventory();
            inventory.loadInventory();
            for (Product product : inventory.getProducts()) {
                inventoryArea.append(product.toString() + "\n");
            }
        });

        modifyButton.addActionListener(e -> new ProductGUI().show());

        addProductButton.addActionListener(e -> inventory.addProduct());

        panel.add(new JScrollPane(inventoryArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(viewButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(addProductButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}
