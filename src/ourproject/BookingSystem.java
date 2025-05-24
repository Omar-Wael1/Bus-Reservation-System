package ourproject;

import java.sql.SQLException;

public class BookingSystem implements AutoCloseable {
    final BusDAO busDAO;
    final UserDAO userDAO;

    public BookingSystem() throws SQLException {
        this.busDAO = new BusDAO();
        this.userDAO = new UserDAO();
    }

    @Override
    public void close() throws SQLException {
        if (busDAO != null) busDAO.close();
        if (userDAO != null) userDAO.close();
    }
}