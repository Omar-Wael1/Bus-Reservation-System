package ourproject;

import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;

public class MainFrame extends JFrame {
    private final BookingSystem bookingSystem;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private AdminPanel adminPanel;
    private PassengerPanel passengerPanel;

    public MainFrame() throws SQLException {
        this.bookingSystem = new BookingSystem();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Bus Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        adminPanel = new AdminPanel(this);
        passengerPanel = new PassengerPanel(this);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(adminPanel, "admin");
        mainPanel.add(passengerPanel, "passenger");

        add(mainPanel);
    }

    public BookingSystem getBookingSystem() {
        return bookingSystem;
    }

    public void setCurrentUser(User user) {
        if (user.getRole().equals("admin")) {
            adminPanel.loadBuses();
            cardLayout.show(mainPanel, "admin");
        } else {
            passengerPanel.loadBuses();
            cardLayout.show(mainPanel, "passenger");
        }
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "login");
    }

    public void closeApplication() {
        try {
            bookingSystem.close();
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error closing system: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}