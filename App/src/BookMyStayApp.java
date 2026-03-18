/**
 * Book My Stay - Hotel Booking Management System
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Demonstrates safe cancellation of confirmed bookings using
 * Stack and controlled rollback to maintain inventory consistency.
 *
 * @author YourName
 * @version 10.0
 */

import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// Inventory class
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
    }

    public void increment(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void decrement(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) - 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> confirmed = new HashMap<>();
    private Stack<String> cancelledStack = new Stack<>();

    public void addReservation(Reservation r) {
        confirmed.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return confirmed.get(id);
    }

    public boolean exists(String id) {
        return confirmed.containsKey(id);
    }

    public void cancelReservation(String id) {
        if (confirmed.containsKey(id)) {
            cancelledStack.push(id);
            confirmed.remove(id);
        }
    }

    public void displayAll() {
        System.out.println("\nConfirmed Reservations:");
        for (Reservation r : confirmed.values()) {
            r.display();
        }
    }

    public Stack<String> getCancelledStack() {
        return cancelledStack;
    }
}

// Cancellation Service
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation Failed: Reservation ID not found.");
            return;
        }

        Reservation r = history.getReservation(reservationId);
        // Rollback inventory
        inventory.increment(r.getRoomType());
        // Remove from history & track cancelled
        history.cancelReservation(reservationId);

        System.out.println("Booking Cancelled for " + r.getGuestName() +
                " (Reservation ID: " + reservationId + ")");
    }

    public void displayCancelledHistory() {
        Stack<String> stack = history.getCancelledStack();
        System.out.println("\nCancelled Reservations (most recent first):");
        for (int i = stack.size() - 1; i >= 0; i--) {
            System.out.println(stack.get(i));
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("=====================================");
        System.out.println("Version: 10.0\n");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Sample confirmed reservations
        Reservation r1 = new Reservation("SR1", "Alice", "Single Room");
        Reservation r2 = new Reservation("DR1", "Bob", "Double Room");
        history.addReservation(r1);
        history.addReservation(r2);

        inventory.decrement(r1.getRoomType());
        inventory.decrement(r2.getRoomType());

        System.out.println("Initial confirmed bookings:");
        history.displayAll();
        inventory.displayInventory();

        // Initialize cancellation service
        CancellationService cancelService = new CancellationService(inventory, history);

        // Cancel a booking
        cancelService.cancelBooking("SR1");

        // Attempt to cancel a non-existent booking
        cancelService.cancelBooking("SR100");

        // Display updated state
        history.displayAll();
        inventory.displayInventory();
        cancelService.displayCancelledHistory();

        System.out.println("\nApplication terminated.");
    }
}