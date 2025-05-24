package ourproject;

import java.awt.*;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JPanel {
    private final MainFrame mainFrame;
    private JTable busesTable;
    private DefaultTableModel tableModel;

    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Admin Dashboard");
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
        
        JButton addBusButton = createStyledButton("Add New Bus", new Color(0, 153, 76));
        addBusButton.addActionListener(e -> showAddBusDialog());
        
        JButton refreshButton = createStyledButton("Refresh", new Color(0, 102, 204));
        refreshButton.addActionListener(e -> loadBuses());
        
        JButton logoutButton = createStyledButton("Logout", new Color(204, 0, 0));
        logoutButton.addActionListener(e -> mainFrame.showLoginPanel());

        buttonPanel.add(addBusButton);
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

    private void showAddBusDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Add New Bus", true);
        dialog.setSize(450, 450);
        dialog.setLayout(new GridLayout(0, 2, 15, 15));
        dialog.getContentPane().setBackground(new Color(240, 240, 240));
        dialog.setLocationRelativeTo(this);

        JTextField busNoField = new JTextField();
        JTextField routeField = new JTextField();
        JTextField seatsField = new JTextField();
        JTextField departureField = new JTextField();
        JTextField arrivalField = new JTextField();
        JTextField priceField = new JTextField();

        dialog.add(new JLabel("Bus Number:"));
        dialog.add(busNoField);
        dialog.add(new JLabel("Route:"));
        dialog.add(routeField);
        dialog.add(new JLabel("Total Seats:"));
        dialog.add(seatsField);
        dialog.add(new JLabel("Departure Time (HH:MM:SS):"));
        dialog.add(departureField);
        dialog.add(new JLabel("Arrival Time (HH:MM:SS):"));
        dialog.add(arrivalField);
        dialog.add(new JLabel("Ticket Price:"));
        dialog.add(priceField);

        JButton addButton = createStyledButton("Add Bus", new Color(0, 153, 76));
        addButton.addActionListener(e -> {
            try {
                Bus bus = new Bus(
                    Integer.parseInt(busNoField.getText()),
                    routeField.getText(),
                    Integer.parseInt(seatsField.getText()),
                    Time.valueOf(departureField.getText()),
                    Time.valueOf(arrivalField.getText()),
                    Double.parseDouble(priceField.getText())
                );
                if (mainFrame.getBookingSystem().busDAO.addBus(bus)) {
                    JOptionPane.showMessageDialog(dialog, "Bus added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadBuses();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = createStyledButton("Cancel", new Color(204, 0, 0));
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(addButton);
        dialog.add(cancelButton);
        dialog.setVisible(true);
    }
}