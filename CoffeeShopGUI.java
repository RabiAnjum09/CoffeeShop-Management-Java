import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.util.ArrayList;

public class CoffeeShopGUI {
    public static final String CUSTOMERS_FILE = "Customers.bin";
    public static final String STAFF_FILE = "Staff.bin";

    private static ArrayList<Staff> staffList = new ArrayList<>();
    private static ArrayList<Customer> customerList = new ArrayList<>();

    public static void main(String[] args) {
        staffList = createDefaultStaff();
        customerList = loadCustomersFromFile(CUSTOMERS_FILE);

        JFrame frame = new JFrame("Coffee Shop App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 350);
        frame.setLocationRelativeTo(null);

        JPanel mainMenuPanel = createMainMenuPanel(frame);
        frame.setContentPane(mainMenuPanel);
        frame.setVisible(true);
    }

    private static JPanel createMainMenuPanel(JFrame frame){
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setBackground(new Color(252, 252, 200));

        ImageIcon logoIcon = new ImageIcon("Coffee.jpeg");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuPanel.add(logoLabel);

        JLabel titleLabel = new JLabel("Welcome to Coffee Shop", JLabel.CENTER);
        titleLabel.setFont(new Font("Broadway", Font.BOLD, 30));
        titleLabel.setForeground(new Color(139, 69, 19));


        JButton staffLoginButton = new JButton("Login as Staff");
        JButton customerLoginButton = new JButton("Login as Customer");
        JButton signUpButton = new JButton("Sign Up as New Customer");
        JButton exitButton = new JButton("Exit");

        customizeButton(staffLoginButton);
        customizeButton(customerLoginButton);
        customizeButton(signUpButton);
        customizeButton(exitButton);

        mainMenuPanel.add(titleLabel);
        mainMenuPanel.add(staffLoginButton);
        mainMenuPanel.add(customerLoginButton);
        mainMenuPanel.add(signUpButton);
        mainMenuPanel.add(exitButton);

        frame.add(mainMenuPanel);
        frame.setVisible(true);

        staffLoginButton.addActionListener(e -> showStaffLogin(frame));
        customerLoginButton.addActionListener(e -> showCustomerLogin(frame));
        signUpButton.addActionListener(e -> showSignUpForm(frame));
        exitButton.addActionListener(e -> {
            saveCustomersToFile(CUSTOMERS_FILE, customerList);
            JOptionPane.showMessageDialog(frame, "Exiting the system. Goodbye!");
            System.exit(0);
        });

        return mainMenuPanel;
    }
    static void customizeButton(JButton button) {
        button.setBackground(new Color(139, 69, 19)); // Beige
        button.setForeground(new Color(255, 255, 198));
        button.setFont(new Font("Cooper Black", Font.PLAIN, 14));
    }

    private static void showStaffLogin(JFrame frame) {
        JPanel panel = new JPanel();
        JTextField staffIdField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton MainMenuButton = new JButton("Back to Main Menu");

        customizeButton(loginButton);
        customizeButton(MainMenuButton);

        panel.add(new JLabel("Staff ID:"));
        panel.add(staffIdField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(MainMenuButton);

        frame.setContentPane(panel);

        frame.revalidate();

        loginButton.addActionListener(e -> {
            try {
                String staffId = staffIdField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (staffId.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Staff ID and Password cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (staffLogin(staffId, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    frame.setVisible(false);
                    new StaffMenuGUI().displayMenu(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Staff ID or Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        MainMenuButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            JPanel mainMenuPanel = createMainMenuPanel(frame);
            frame.setContentPane(mainMenuPanel);
            frame.revalidate();
            frame.repaint();
        });
    }

    private static void showSignUpForm(JFrame mainMenuFrame) {
        JPanel panel = new JPanel();
        JTextField nameField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField cityField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton signUpButton = new JButton("Sign Up");
        JButton MainMenuButton = new JButton("Back to Main Menu");

        customizeButton(signUpButton);
        customizeButton(MainMenuButton);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(signUpButton);


        JFrame signUpFrame = new JFrame("Sign Up");
        signUpFrame.setSize(400, 300);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.add(panel);
        signUpFrame.setVisible(true);
        signUpFrame.setLocationRelativeTo(null);

        signUpButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            String city = cityField.getText().trim();
            String phone = phoneField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(signUpFrame, "Name is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidName(name)) {
                JOptionPane.showMessageDialog(signUpFrame, "Invalid name! Only alphabets and spaces are allowed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(signUpFrame, "Address is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidAddressOrCity(address)) {
                JOptionPane.showMessageDialog(signUpFrame, "Invalid address! Only alphabets, spaces, and commas are allowed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(signUpFrame, "City is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidAddressOrCity(city)) {
                JOptionPane.showMessageDialog(signUpFrame, "Invalid city! Only alphabets and spaces are allowed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(signUpFrame, "Phone number is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(signUpFrame, "Invalid phone number! Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(signUpFrame, "Password is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(signUpFrame, "Password must be at least 8 characters long.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer newCustomer = new Customer(name, address, city, phone, password);
            customerList.add(newCustomer);
            saveCustomersToFile(CUSTOMERS_FILE, customerList);

            JOptionPane.showMessageDialog(signUpFrame, "Sign-up successful!");
            signUpFrame.dispose();
            mainMenuFrame.setVisible(true);
        });
    }

    private static void showCustomerLogin(JFrame frame) {
        JPanel panel = new JPanel();
        JTextField nameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton MainMenuButton = new JButton("Back to Main Menu");

        customizeButton(loginButton);
        customizeButton(MainMenuButton);

        panel.add(new JLabel("Customer Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(MainMenuButton);

        frame.setContentPane(panel);
        frame.revalidate();

        loginButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                if (name.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Name and Password cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (customerLogin(name, password)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    frame.setVisible(false);
                    CustomerMenuGUI customerMenu = new CustomerMenuGUI();
                    customerMenu.displayMenu(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Name or Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        MainMenuButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            JPanel mainMenuPanel = createMainMenuPanel(frame);
            frame.setContentPane(mainMenuPanel);
            frame.revalidate();
            frame.repaint();
        });
    }

    private static boolean staffLogin(String staffId, String password) {
        return staffList.stream().anyMatch(staff ->
                staff.getId().equalsIgnoreCase(staffId) && staff.getPassword().equals(password));
    }

    private static boolean customerLogin(String name, String password) {
        return customerList.stream().anyMatch(customer ->
                customer.getName().equalsIgnoreCase(name) && customer.getPassword().equals(password));
    }

    private static ArrayList<Staff> createDefaultStaff() {
        ArrayList<Staff> list = new ArrayList<>();
        list.add(new Staff("Staff1", "password1"));
        list.add(new Staff("Staff2", "password2"));
        return list;
    }

    private static ArrayList<Customer> loadCustomersFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (ArrayList<Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private static void saveCustomersToFile(String fileName, ArrayList<Customer> customerList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(customerList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches(".*[A-Za-z].*");
    }

    public static boolean isValidAddressOrCity(String input) {
        return input != null && !input.trim().isEmpty() || input.matches(".[A-Za-z].") ||
                input.matches(".\\d.") || input.matches(".[!@#$%^&(),.?\":{}|<>].*");
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("\\d{11}");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 || password.matches(".[A-Za-z].") ||
                password.matches(".\\d.") || password.matches(".[!@#$%^&(),.?\":{}|<>].*");
    }
}