package ourproject;

import java.sql.*;
import javax.swing.JOptionPane;

public class UserDAO implements AutoCloseable {
    private final Connection connection;
    private User currentUser;

    public UserDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            return stmt.executeUpdate() > 0;
        }
    }

    public User authenticate(String username, String password) throws SQLException {
        String checkSql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, 
                    "Username not found. Please register first.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentUser = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role")
                );
                return currentUser;
            }
            JOptionPane.showMessageDialog(null, "Invalid password", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) connection.close();
    }
}