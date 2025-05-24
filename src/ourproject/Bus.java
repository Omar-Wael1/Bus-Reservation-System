package ourproject;

import java.sql.Time;

public class Bus {
    int id;
    private final int busNo;
    private String route;
    private final int totalSeats;
    int availableSeats;
    private Time departureTime;
    private Time arrivalTime;
    private double price;

    public Bus(int busNo, String route, int totalSeats, Time departureTime, Time arrivalTime, double price) {
        this.busNo = busNo;
        this.route = route;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
    }

    public Bus(int busNo, int totalSeats) {
        this.busNo = busNo;
        this.totalSeats = totalSeats;
    }

    public int getId() { return id; }
    public int getBusNo() { return busNo; }
    public String getRoute() { return route; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public Time getDepartureTime() { return departureTime; }
    public Time getArrivalTime() { return arrivalTime; }
    public double getPrice() { return price; }

    public void showDetails() {
        System.out.printf("Bus No: %d | Route: %s | Departure: %s | Arrival: %s | Price: %.2f | Available Seats: %d/%d%n",
                busNo, route, departureTime, arrivalTime, price, availableSeats, totalSeats);
    }

    public void setRoute(String route) {
        this.route = route;
    }
}