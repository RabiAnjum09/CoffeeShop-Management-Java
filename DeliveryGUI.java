import javax.swing.*;
import java.awt.*;

public class DeliveryGUI {
    private Delivery delivery;

    public void show() {
        JFrame frame = new JFrame("Delivery Management");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        JLabel nameLabel = new JLabel("Recipient Name:");
        JTextField nameField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JLabel cityLabel = new JLabel("City:");
        JTextField cityField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField();

        JButton saveButton = new JButton("Save Delivery Details");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String address = addressField.getText();
            String city = cityField.getText();
            String phone = phoneField.getText();
            delivery = new Delivery(name, address, city, phone);
            JOptionPane.showMessageDialog(frame, "Delivery details saved!");
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(cityLabel);
        panel.add(cityField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(saveButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}
