/**
 * Book My Stay - Hotel Booking Management System
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Demonstrates the use of HashMap to manage room availability
 * as a single source of truth.
 *
 * @author YourName
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

// Inventory management class
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor initializes room availability
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability of a specific room type
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("----- Current Room Inventory -----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("=====================================");
        System.out.println("Version: 3.1\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Retrieve availability
        System.out.println("\nChecking availability for Single Room: "
                + inventory.getAvailability("Single Room"));

        // Update availability
        System.out.println("\nUpdating availability...");
        inventory.updateAvailability("Single Room", 4);

        // Display updated inventory
        System.out.println();
        inventory.displayInventory();

        System.out.println("\nApplication terminated.");
    }
}