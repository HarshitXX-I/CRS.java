import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
}

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

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
        } else {
            JOptionPane.showMessageDialog(null, "Car is not available for rent. Please choose another option.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            JOptionPane.showMessageDialog(null, "Car was not rented.");
        }
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

    public List<Rental> getRentals() {
        return rentals;
    }
}

public class CarRentalGUI extends JFrame {
    private CarRentalSystem rentalSystem;
    private JTextArea outputArea;
    private JTextField customerNameField;
    private JTextField carIdField;
    private JTextField rentalDaysField;

    public CarRentalGUI() {
        rentalSystem = new CarRentalSystem();
        initializeCars();
        createUI();
    }

    private void initializeCars() {
        rentalSystem.addCar(new Car("ID_001", "BMW", "BMW X3", 2600.0));
        rentalSystem.addCar(new Car("ID_002", "AUDI", "AUDI Q3", 2400.0));
        rentalSystem.addCar(new Car("ID_003", "Mahindra", "Thar", 1200.0));
        rentalSystem.addCar(new Car("ID_004", "Suzuki", "Brezza", 450.0));
        rentalSystem.addCar(new Car("ID_005", "Hyundai", "I 10", 600.0));
    }

    private void createUI() {
        setTitle("Car Rental System");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Rental Controls"));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);

        // Customer Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Customer Name:"), gbc);
        customerNameField = new JTextField(15);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(customerNameField, gbc);

        // Car ID
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Car ID:"), gbc);
        carIdField = new JTextField(15);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(carIdField, gbc);

        // Rental Days
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Rental Days:"), gbc);
        rentalDaysField = new JTextField(15);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(rentalDaysField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton rentButton = new JButton("Rent Car");
        rentButton.addActionListener(new RentCarAction());
        buttonPanel.add(rentButton);

        JButton returnButton = new JButton("Return Car");
        returnButton.addActionListener(new ReturnCarAction());
        buttonPanel.add(returnButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private class RentCarAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String customerName = customerNameField.getText().trim();
            String carId = carIdField.getText().trim();
            int rentalDays;

            if (customerName.isEmpty()) {
                JOptionPane.showMessageDialog(CarRentalGUI.this, "Please enter a customer name.");
                return;
            }
            if (carId.isEmpty()) {
                JOptionPane.showMessageDialog(CarRentalGUI.this, "Please enter a car ID.");
                return;
            }
            try {
                rentalDays = Integer.parseInt(rentalDaysField.getText().trim());
                if (rentalDays <= 0) {
                    JOptionPane.showMessageDialog(CarRentalGUI.this, "Rental days must be positive.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(CarRentalGUI.this, "Please enter a valid number for rental days.");
                return;
            }

            // Check if car is available
            Car selectedCar = null;
            for (Car car : rentalSystem.getAvailableCars()) {
                if (car.getCarId().equalsIgnoreCase(carId)) {
                    selectedCar = car;
                    break;
                }
            }

            if (selectedCar == null) {
                outputArea.append("Invalid car selection or car not available for rent.\n");
                return;
            }

            // Create new customer with unique id
            Customer newCustomer = new Customer("CUS" + (rentalSystem.getRentals().size() + rentalSystem.customers.size() + 1), customerName);
            rentalSystem.addCustomer(newCustomer);

            // Confirm rental details
            double totalPrice = selectedCar.calculatePrice(rentalDays);
            int confirm = JOptionPane.showConfirmDialog(CarRentalGUI.this,
                    "Customer ID: " + newCustomer.getCustomerId() + "\n" +
                            "Customer Name: " + newCustomer.getName() + "\n" +
                            "Car: " + selectedCar.getBrand() + " " + selectedCar.getModel() + "\n" +
                            "Rental Days: " + rentalDays + "\n" +
                            String.format("Total Price: $%.2f%n", totalPrice) +
                            "Confirm rental?",
                    "Confirm Rental",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                rentalSystem.rentCar(selectedCar, newCustomer, rentalDays);
                outputArea.append("Car rented successfully: " + selectedCar.getBrand() + " " + selectedCar.getModel() + "\n");
            } else {
                outputArea.append("Rental canceled.\n");
            }
        }
    }

    private class ReturnCarAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String carId = carIdField.getText().trim();
            if (carId.isEmpty()) {
                JOptionPane.showMessageDialog(CarRentalGUI.this, "Please enter a car ID to return.");
                return;
            }

            Car carToReturn = null;

            // Find the car in the list of rentals (not available cars)
            for (Rental rental : rentalSystem.getRentals()) {
                if (rental.getCar().getCarId().equalsIgnoreCase(carId)) {
                    carToReturn = rental.getCar();
                    break;
                }
            }

            if (carToReturn != null) {
                rentalSystem.returnCar(carToReturn);
                outputArea.append("Car returned successfully: " + carToReturn.getBrand() + " " + carToReturn.getModel() + "\n");
            } else {
                outputArea.append("Invalid car ID or car is not rented.\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarRentalGUI gui = new CarRentalGUI();
            gui.setVisible(true);
        });
    }
}
