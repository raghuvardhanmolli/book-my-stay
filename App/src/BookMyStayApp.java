/**
 * Book My Stay - Hotel Booking Management System
 *
 * Use Case 7: Add-On Service Selection
 *
 * Demonstrates attaching optional services to reservations
 * using Map and List without modifying core booking logic.
 *
 * @author YourName
 * @version 7.0
 */

import java.util.*;

// Add-On Service class
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

// Manager for Add-On Services
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service: " + service.getName() +
                " to Reservation: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        System.out.println("\nServices for Reservation: " + reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s.getName() + " (₹" + s.getCost() + ")");
        }
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);
        double total = 0.0;

        if (services != null) {
            for (AddOnService s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("=====================================");
        System.out.println("Version: 7.0\n");

        // Sample reservation IDs (from previous use case)
        String res1 = "SR1";
        String res2 = "DR1";

        // Create services
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService breakfast = new AddOnService("Breakfast", 300);
        AddOnService parking = new AddOnService("Parking", 150);

        // Initialize manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services to reservations
        manager.addService(res1, wifi);
        manager.addService(res1, breakfast);

        manager.addService(res2, parking);

        // Display services
        manager.displayServices(res1);
        System.out.println("Total Add-On Cost: ₹" + manager.calculateTotalCost(res1));

        manager.displayServices(res2);
        System.out.println("Total Add-On Cost: ₹" + manager.calculateTotalCost(res2));

        System.out.println("\nCore booking and inventory remain unchanged.");
        System.out.println("Application terminated.");
    }
}