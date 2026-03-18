/**
 * Book My Stay - Hotel Booking Management System
 *
 * Use Case 9: Error Handling & Validation
 *
 * Demonstrates validation, custom exceptions, and fail-fast design
 * to ensure system reliability and prevent invalid state changes.
 *
 * @author YourName
 * @version 9.0
 */

import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory class
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) throws InvalidBookingException {
        int available = getAvailability(type);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for " + type);
        }

        inventory.put(type, available - 1);
    }
}

// Validator class
class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "Room not available: " + r.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    public void processBooking(Reservation r, RoomInventory inventory) {

        try {
            // Validate first (Fail-Fast)
            BookingValidator.validate(r, inventory);

            // Allocate room (safe)
            inventory.decrement(r.getRoomType());

            System.out.println("Booking Successful for " + r.getGuestName() +
                    " (" + r.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("=====================================");
        System.out.println("Version: 9.0\n");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService();

        // Valid booking
        service.processBooking(new Reservation("Alice", "Single Room"), inventory);

        // Invalid room type
        service.processBooking(new Reservation("Bob", "Luxury Room"), inventory);

        // No availability case
        service.processBooking(new Reservation("Charlie", "Single Room"), inventory);

        // Invalid guest name
        service.processBooking(new Reservation("", "Double Room"), inventory);

        System.out.println("\nSystem remains stable after handling errors.");
        System.out.println("Application terminated.");
    }
}