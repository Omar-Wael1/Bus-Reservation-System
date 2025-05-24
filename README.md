# Bus Reservation System

A complete Java Swing application for bus reservation management with admin and passenger interfaces.

## Features
- User authentication (login/register) with different roles (admin/passenger)
- Admin panel for managing buses (add new buses, view all buses)
- Passenger panel for viewing available buses and booking tickets
- Database integration with MySQL
- Clean, responsive UI with Swing components

## Technologies
- Java 
- Swing for GUI
- MySQL for database
- JDBC for database connectivity

## Database Setup
1. Create a MySQL database named `bus_reservation`
2. Run the SQL scripts to create necessary tables (buses, users, bookings)

## How to Run
1. Clone this repository
2. Configure database connection in `DatabaseConnection.java`
3. Run `BusReservationAppGUI.java`
