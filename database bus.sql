CREATE DATABASE IF NOT EXISTS bus_reservation;
USE bus_reservation;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('admin', 'passenger') NOT NULL DEFAULT 'passenger',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS buses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    bus_no INT NOT NULL,
    route VARCHAR(100) NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    bus_no INT NOT NULL,
    user_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    seat_number INT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status ENUM('pending', 'paid', 'cancelled') DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (bus_no) REFERENCES buses(id)
);

CREATE TABLE IF NOT EXISTS payments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100),
    status ENUM('pending', 'completed', 'failed') NOT NULL DEFAULT 'pending',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

CREATE INDEX idx_buses_route ON buses(route);
CREATE INDEX idx_buses_departure ON buses(departure_time);
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_bus ON bookings(bus_no);


INSERT INTO users (username, password, email, role)
VALUES ('admin', 'admin123', 'admin@bus.com', 'admin');


INSERT INTO users (username, password, email, role)
VALUES ('passenger1', 'pass123', 'passenger1@email.com', 'passenger');


INSERT INTO buses (bus_no, route, total_seats, available_seats, departure_time, arrival_time, price)
VALUES 
(1001, 'Cairo to Alexandria', 50, 50, '08:00:00', '12:00:00', 150.50),
(1002, 'Cairo to Luxor', 40, 40, '22:00:00', '06:00:00', 300.00),
(1003, 'Alexandria to Sharm El Sheikh', 30, 30, '07:30:00', '16:45:00', 250.75);