# Bus Reservation System

A complete Java Swing application for bus reservation management with admin and passenger interfaces.

## Features
- **User Authentication**: Login/Register system with role-based access (Admin/Passenger)
- **Admin Panel**: 
  - Add new buses with complete details
  - View all available buses
  - Manage bus inventory
- **Passenger Panel**:
  - Browse available buses
  - Book tickets with seat selection
  - Payment processing simulation
- **Database Integration**: 
  - MySQL database with JDBC
  - Secure user authentication
  - Transaction management for bookings

## Core Components
- `MainFrame`: Main application window with card layout
- `LoginPanel`: Handles user authentication
- `AdminPanel`: Bus management interface
- `PassengerPanel`: Ticket booking interface
- `BookingSystem`: Core system controller
- `BusDAO`: Data access for bus operations
- `UserDAO`: Handles user authentication and registration
- `PaymentService`: Simulates payment processing

## Database Schema
- **users**: Stores user credentials and roles
- **buses**: Contains bus schedule and availability
- **bookings**: Records all ticket reservations

## Technologies
- Java 
- Swing for GUI
- MySQL for database
- JDBC for database connectivity

## How to Run
1. Create MySQL database `bus_reservation`
2. Execute SQL scripts to create tables
3. Configure `DatabaseConnection.java` with your credentials
4. Run `BusReservationAppGUI.java`
