package ourproject;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PassengerPanel extends JPanel {
    private final MainFrame mainFrame;
    private JTable busesTable;
    private DefaultTableModel tableModel;

    public PassengerPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Passenger Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Bus No", "Route", "Departure", "Arrival", "Available Seats", "Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        busesTable = new JTable(tableModel);
        busesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        busesTable.setRowHeight(25);
        busesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(busesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton bookButton = createStyledButton("Book Ticket", new Color(0, 153, 76));
        bookButton.addActionListener(e -> showBookingDialog());
        
        JButton refreshButton = createStyledButton("Refresh", new Color(0, 102, 204));
        refreshButton.addActionListener(e -> loadBuses());
        
        JButton logoutButton = createStyledButton("Logout", new Color(204, 0, 0));
        logoutButton.addActionListener(e -> mainFrame.showLoginPanel());

        buttonPanel.add(bookButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }

    public void loadBuses() {
        try {
            List<Bus> buses = mainFrame.getBookingSystem().busDAO.getAllBuses();
            tableModel.setRowCount(0);

            for (Bus bus : buses) {
                Object[] row = {
                    bus.getId(),
                    bus.getBusNo(),
                    bus.getRoute(),
                    bus.getDepartureTime(),
                    bus.getArrivalTime(),
                    bus.getAvailableSeats() + "/" + bus.getTotalSeats(),
                    String.format("$%.2f", bus.getPrice())
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading buses: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBookingDialog() {
        int selectedRow = busesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bus first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int busId = (int) tableModel.getValueAt(selectedRow, 0);
        double price = Double.parseDouble(tableModel.getValueAt(selectedRow, 6).toString().replace("$", ""));

        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Book Ticket", true);
        dialog.setSize(450, 400);
        dialog.setLayout(new GridLayout(0, 2, 15, 15));
        dialog.getContentPane().setBackground(new Color(240, 240, 240));
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JTextField seatField = new JTextField();
        JComboBox<String> paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "PayPal", "Cash"});
        JLabel amountValue = new JLabel(String.format("$%.2f", price));

        dialog.add(new JLabel("Passenger Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Seat Number:"));
        dialog.add(seatField);
        dialog.add(new JLabel("Payment Method:"));
        dialog.add(paymentMethodCombo);
        dialog.add(new JLabel("Amount:"));
        dialog.add(amountValue);

        JButton bookButton = createStyledButton("Confirm Booking", new Color(0, 153, 76));
        bookButton.addActionListener(e -> {
            try {
                String passengerName = nameField.getText();
                int seatNumber = Integer.parseInt(seatField.getText());
                String paymentMethod = (String) paymentMethodCombo.getSelectedItem();

                PaymentService paymentService = new PaymentService();
                if (paymentService.processPayment(price, paymentMethod) &&
                    mainFrame.getBookingSystem().busDAO.bookSeat(
                        busId, 
                        mainFrame.getBookingSystem().userDAO.getCurrentUser().getId(),
                        passengerName, 
                        seatNumber, 
                        "paid")) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Booking successful!\nTransaction ID: " + paymentService.generateTransactionId(), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadBuses();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = createStyledButton("Cancel", new Color(204, 0, 0));
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(bookButton);
        dialog.add(cancelButton);
        dialog.setVisible(true);
    }
}