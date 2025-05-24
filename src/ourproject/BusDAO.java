package ourproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusDAO implements AutoCloseable {
    private final Connection connection;

    public BusDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean addBus(Bus bus) throws SQLException {
        String sql = "INSERT INTO buses (bus_no, route, total_seats, available_seats, departure_time, arrival_time, price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bus.getBusNo());
            stmt.setString(2, bus.getRoute());
            stmt.setInt(3, bus.getTotalSeats());
            stmt.setInt(4, bus.getAvailableSeats());
            stmt.setTime(5, bus.getDepartureTime());
            stmt.setTime(6, bus.getArrivalTime());
            stmt.setDouble(7, bus.getPrice());
            
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Bus> getAllBuses() throws SQLException {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT * FROM buses WHERE is_active = TRUE";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bus bus = new Bus(
                    rs.getInt("bus_no"),
                    rs.getString("route"),
                    rs.getInt("total_seats"),
                    rs.getTime("departure_time"),
                    rs.getTime("arrival_time"),
                    rs.getDouble("price")
                );
                bus.id = rs.getInt("id");
                bus.availableSeats = rs.getInt("available_seats");
                buses.add(bus);
            }
        }
        return buses;
    }

    public Bus getBusById(int busId) throws SQLException {
        String sql = "SELECT * FROM buses WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, busId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Bus bus = new Bus(
                    rs.getInt("bus_no"),
                    rs.getString("route"),
                    rs.getInt("total_seats"),
                    rs.getTime("departure_time"),
                    rs.getTime("arrival_time"),
                    rs.getDouble("price")
                );
                bus.id = rs.getInt("id");
                bus.availableSeats = rs.getInt("available_seats");
                return bus;
            }
        }
        return null;
    }

    public boolean bookSeat(int busId, int userId, String passengerName, int seatNumber, String paymentStatus) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String checkSql = "SELECT available_seats FROM buses WHERE id = ? FOR UPDATE";
            try (PreparedStatement stmt = connection.prepareStatement(checkSql)) {
                stmt.setInt(1, busId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next() && rs.getInt("available_seats") > 0) {
                    String updateSql = "UPDATE buses SET available_seats = available_seats - 1 WHERE id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, busId);
                        updateStmt.executeUpdate();
                    }

                    String insertSql = "INSERT INTO bookings (bus_no, user_id, passenger_name, seat_number, payment_status) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, busId);
                        insertStmt.setInt(2, userId);
                        insertStmt.setString(3, passengerName);
                        insertStmt.setInt(4, seatNumber);
                        insertStmt.setString(5, paymentStatus);
                        insertStmt.executeUpdate();
                    }

                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}