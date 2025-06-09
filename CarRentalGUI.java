import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents a car available for rent.
 */
class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }

    @Override
    public String toString() {
        return String.format("%s (%s %s) - %s", carId, brand, model, (isAvailable ? "Available" : "Rented"));
    }
}

/**
 * Represents a customer renting a car.
 */
class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

/**
 * Represents a rental transaction linking a car and a customer.
 */
class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

/**
 * Core system managing cars, customers, and rentals.
 */
class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public int getCustomerCount() {
        return customers.size();
    }

    public boolean rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
            return true;
        }
        return false;
    }

    public boolean returnCar(Car car) {
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                car.returnCar();
                rentals.remove(rental);
                return true;
            }
        }
        return false;
    }

    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }

    public List<Rental> getRentals() {
        return rentals;
    }
}

/**
 * GUI for Car Rental System using Swing.
 */
public class CarRentalGUI extends JFrame {
    private CarRentalSystem rentalSystem;

    private JTextArea outputArea;
    private JTextField customerNameField;
    private JTextField carIdField;
    private JTextField rentalDaysField;

    private JButton rentButton;
    private JButton returnButton;
    private JButton showCarsButton;

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font OUTPUT_FONT = new Font("Consolas", Font.PLAIN, 14);

    private static final Color LIGHT_BG = Color.WHITE;
    private static final Color LIGHT_FG = new Color(40, 40, 40);
    private static final Color LIGHT_BUTTON_BG = new Color(33, 150, 243);
    private static final Color LIGHT_BUTTON_FG = Color.WHITE;
    private static final Color LIGHT_BUTTON_RETURN_BG = new Color(76, 175, 80);
    private static final Color LIGHT_BUTTON_SHOW_BG = new Color(158, 158, 158);

    private static final Color DARK_BG = new Color(34, 34, 34);
    private static final Color DARK_FG = new Color(230, 230, 230);
    private static final Color DARK_BUTTON_BG = new Color(10, 132, 255);
    private static final Color DARK_BUTTON_FG = Color.WHITE;
    private static final Color DARK_BUTTON_RETURN_BG = new Color(48, 209, 88);
    private static final Color DARK_BUTTON_SHOW_BG = new Color(100, 100, 100);

    private boolean isDarkMode = false;

    public CarRentalGUI() {
        rentalSystem = new CarRentalSystem();
        initializeCars();
        initializeUI();
    }

    private void initializeCars() {
        rentalSystem.addCar(new Car("ID_001", "BMW", "BMW X3", 2600.0));
        rentalSystem.addCar(new Car("ID_002", "AUDI", "AUDI Q3", 2400.0));
        rentalSystem.addCar(new Car("ID_003", "Mahindra", "Thar", 1200.0));
        rentalSystem.addCar(new Car("ID_004", "Suzuki", "Brezza", 450.0));
        rentalSystem.addCar(new Car("ID_005", "Hyundai", "I 10", 600.0));
    }

    private void initializeUI() {
        setTitle("Car Rental System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(LIGHT_BG);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(OUTPUT_FONT);
        outputArea.setMargin(new Insets(10,10,10,10));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBackground(new Color(250,250,250));
        outputArea.setForeground(LIGHT_FG);

        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true),
                "System Output",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                LABEL_FONT,
                new Color(75,75,75)
        ));
        outputScrollPane.setPreferredSize(new Dimension(580, 280));
        add(outputScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(LIGHT_BG);
        inputPanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        "Rental Controls",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        LABEL_FONT,
                        new Color(60, 60, 60)
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameLabel.setFont(LABEL_FONT);
        customerNameLabel.setForeground(LIGHT_FG);
        inputPanel.add(customerNameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        customerNameField = new JTextField();
        customerNameField.setFont(INPUT_FONT);
        customerNameField.setToolTipText("Enter the full name of the customer");
        inputPanel.add(customerNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel carIdLabel = new JLabel("Car ID:");
        carIdLabel.setFont(LABEL_FONT);
        carIdLabel.setForeground(LIGHT_FG);
        inputPanel.add(carIdLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        carIdField = new JTextField();
        carIdField.setFont(INPUT_FONT);
        carIdField.setToolTipText("Enter the ID of the car to rent/return");
        inputPanel.add(carIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel rentalDaysLabel = new JLabel("Rental Days:");
        rentalDaysLabel.setFont(LABEL_FONT);
        rentalDaysLabel.setForeground(LIGHT_FG);
        inputPanel.add(rentalDaysLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        rentalDaysField = new JTextField();
        rentalDaysField.setFont(INPUT_FONT);
        rentalDaysField.setToolTipText("Enter number of days to rent");
        inputPanel.add(rentalDaysField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setBackground(LIGHT_BG);

        rentButton = new JButton("Rent Car");
        rentButton.setFont(LABEL_FONT);
        rentButton.setFocusPainted(false);
        rentButton.setBorder(new RoundedBorder(10));
        rentButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rentButton.setToolTipText("Rent the car to the customer");
        rentButton.addActionListener(e -> handleRentCar());

        returnButton = new JButton("Return Car");
        returnButton.setFont(LABEL_FONT);
        returnButton.setFocusPainted(false);
        returnButton.setBorder(new RoundedBorder(10));
        returnButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        returnButton.setToolTipText("Return the rented car");
        returnButton.addActionListener(e -> handleReturnCar());

        showCarsButton = new JButton("Show All Cars");
        showCarsButton.setFont(LABEL_FONT);
        showCarsButton.setFocusPainted(false);
        showCarsButton.setBorder(new RoundedBorder(10));
        showCarsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showCarsButton.setToolTipText("Display all cars and availability");
        showCarsButton.addActionListener(e -> displayAllCars());

        buttonPanel.add(rentButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(showCarsButton);

        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.SOUTH);

        // Add toggle theme button to top right corner
        JButton toggleThemeButton = new JButton("Toggle Dark Mode");
        toggleThemeButton.setFont(INPUT_FONT);
        toggleThemeButton.setBackground(new Color(238, 238, 238));
        toggleThemeButton.setFocusPainted(false);
        toggleThemeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleThemeButton.setBorder(new RoundedBorder(8));
        toggleThemeButton.addActionListener(e -> toggleTheme());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        topPanel.setBackground(LIGHT_BG);
        topPanel.add(toggleThemeButton);
        add(topPanel, BorderLayout.NORTH);

        appendOutput("Welcome to the Car Rental System!\n");
        appendOutput("Enter the details above, then press Rent or Return.\n");
        appendOutput("Use 'Show All Cars' to view current inventory.\n");

        // Apply initial theme (light)
        applyTheme();
    }

    private void applyTheme() {
        Color bg = isDarkMode ? DARK_BG : LIGHT_BG;
        Color fg = isDarkMode ? DARK_FG : LIGHT_FG;
        Color outputBg = isDarkMode ? new Color(45, 45, 45) : new Color(250, 250, 250);

        getContentPane().setBackground(bg);

        outputArea.setBackground(outputBg);
        outputArea.setForeground(fg);

        // Update label colors in input panel
        for (Component comp : ((JPanel)getContentPane().getComponent(1)).getComponents()) {
            if (comp instanceof JPanel) {
                for (Component innerComp : ((JPanel) comp).getComponents()) {
                    if (innerComp instanceof JLabel) {
                        innerComp.setForeground(fg);
                    }
                    if (innerComp instanceof JTextField) {
                        innerComp.setBackground(isDarkMode ? new Color(60, 60, 60) : Color.WHITE);
                        innerComp.setForeground(fg);
                        ((JTextField)innerComp).setCaretColor(fg);
                    }
                }
            }
        }

        // Update buttons colors
        if (isDarkMode) {
            rentButton.setBackground(DARK_BUTTON_BG);
            rentButton.setForeground(DARK_BUTTON_FG);
            returnButton.setBackground(DARK_BUTTON_RETURN_BG);
            returnButton.setForeground(DARK_BUTTON_FG);
            showCarsButton.setBackground(DARK_BUTTON_SHOW_BG);
            showCarsButton.setForeground(DARK_BUTTON_FG);
        } else {
            rentButton.setBackground(LIGHT_BUTTON_BG);
            rentButton.setForeground(LIGHT_BUTTON_FG);
            returnButton.setBackground(LIGHT_BUTTON_RETURN_BG);
            returnButton.setForeground(LIGHT_BUTTON_FG);
            showCarsButton.setBackground(LIGHT_BUTTON_SHOW_BG);
            showCarsButton.setForeground(LIGHT_BUTTON_FG);
        }

        // Update input panel background color
        Component inputPanel = getContentPane().getComponent(2);
        if (inputPanel instanceof JPanel) {
            ((JPanel)inputPanel).setBackground(bg);
            Border titledBorder = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(isDarkMode ? new Color(100,100,100) : new Color(200, 200, 200), 1, true),
                    "Rental Controls",
                    javax.swing.border.TitledBorder.LEFT,
                    javax.swing.border.TitledBorder.TOP,
                    LABEL_FONT,
                    fg
            );
            ((JPanel)inputPanel).setBorder(new CompoundBorder(
                    titledBorder,
                    new EmptyBorder(15, 15, 15, 15)
            ));
            // Also update label colors inside inputPanel
            for (Component c : ((JPanel)inputPanel).getComponents()) {
                if (c instanceof JLabel) {
                    c.setForeground(fg);
                }
            }
        }
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
    }

    private void appendOutput(String message) {
        outputArea.append(message);
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void handleRentCar() {
        String customerName = customerNameField.getText().trim();
        String carId = carIdField.getText().trim();
        String rentalDaysText = rentalDaysField.getText().trim();

        if (!isValidCustomerName(customerName)) {
            showErrorMessage("Please enter a valid customer name (letters and spaces only).");
            return;
        }
        if (carId.isEmpty()) {
            showErrorMessage("Please enter a car ID.");
            return;
        }

        int rentalDays;
        try {
            rentalDays = Integer.parseInt(rentalDaysText);
            if (rentalDays <= 0) {
                showErrorMessage("Rental days must be a positive integer.");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter a valid number for rental days.");
            return;
        }

        Car selectedCar = findAvailableCarById(carId);
        if (selectedCar == null) {
            appendOutput("Invalid car ID or car not available for rent.\n");
            return;
        }

        String uniqueCustomerId = "CUS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Customer newCustomer = new Customer(uniqueCustomerId, customerName);
        rentalSystem.addCustomer(newCustomer);

        double totalPrice = selectedCar.calculatePrice(rentalDays);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Customer ID: " + newCustomer.getCustomerId() + "\n" +
                        "Customer Name: " + newCustomer.getName() + "\n" +
                        "Car: " + selectedCar.getBrand() + " " + selectedCar.getModel() + "\n" +
                        "Rental Days: " + rentalDays + "\n" +
                        String.format("Total Price: $%.2f%n", totalPrice) +
                        "Confirm rental?",
                "Confirm Rental",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = rentalSystem.rentCar(selectedCar, newCustomer, rentalDays);
            if (success) {
                appendOutput(String.format("Car rented successfully: %s %s for %d days. Total: $%.2f\n",
                        selectedCar.getBrand(), selectedCar.getModel(), rentalDays, totalPrice));
                clearInputFields();
            } else {
                showErrorMessage("Failed to rent the car. It may no longer be available.");
            }
        } else {
            appendOutput("Rental canceled by user.\n");
        }
    }

    private void handleReturnCar() {
        String carId = carIdField.getText().trim();
        if (carId.isEmpty()) {
            showErrorMessage("Please enter a car ID to return.");
            return;
        }

        Car rentedCar = findRentedCarById(carId);
        if (rentedCar == null) {
            appendOutput("Invalid car ID or the car is not currently rented.\n");
            return;
        }

        boolean returned = rentalSystem.returnCar(rentedCar);
        if (returned) {
            appendOutput(String.format("Car returned successfully: %s %s\n", rentedCar.getBrand(), rentedCar.getModel()));
            clearInputFields();
        } else {
            showErrorMessage("Failed to return the car. Please check the car ID.");
        }
    }

    private void displayAllCars() {
        List<Car> cars = rentalSystem.getAllCars();
        StringBuilder builder = new StringBuilder();
        builder.append("Cars Inventory:\n");
        for (Car car : cars) {
            builder.append(String.format("- %s: %s %s - %s\n",
                    car.getCarId(), car.getBrand(), car.getModel(), car.isAvailable() ? "Available" : "Rented"));
        }
        appendOutput(builder.toString());
    }

    private Car findAvailableCarById(String carId) {
        for (Car car : rentalSystem.getAvailableCars()) {
            if (car.getCarId().equalsIgnoreCase(carId)) {
                return car;
            }
        }
        return null;
    }

    private Car findRentedCarById(String carId) {
        for (Rental rental : rentalSystem.getRentals()) {
            if (rental.getCar().getCarId().equalsIgnoreCase(carId)) {
                return rental.getCar();
            }
        }
        return null;
    }

    private boolean isValidCustomerName(String name) {
        if (name.isEmpty()) {
            return false;
        }
        return Pattern.matches("[a-zA-Z\\s\\-']+", name);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void clearInputFields() {
        customerNameField.setText("");
        carIdField.setText("");
        rentalDaysField.setText("");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            CarRentalGUI gui = new CarRentalGUI();
            gui.setVisible(true);
        });
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius+1, radius+1, radius+1, radius+1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius+1;
            return insets;
        }
    }
}

