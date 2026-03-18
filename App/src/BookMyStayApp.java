/**
 * Book My Stay - Hotel Booking Management System
 *
 * Use Case 5: Booking Request (First-Come-First-Served)
 *
 * Demonstrates handling booking requests using a Queue (FIFO)
 * to ensure fairness and proper ordering.
 *
 * @author YourName
 * @version 5.0
 */

import java.util.*;

// Reservation class representing a booking request
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue class
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayRequests() {
        System.out.println("\n----- Booking Request Queue -----");
        for (Reservation r : queue) {
            r.display();
        }
    }

    // Get next request (without removing)
    public Reservation peekNextRequest() {
        return queue.peek();
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("=====================================");
        System.out.println("Version: 5.0\n");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display queued requests
        bookingQueue.displayRequests();

        // Peek next request
        Reservation next = bookingQueue.peekNextRequest();
        if (next != null) {
            System.out.println("\nNext request to process:");
            next.display();
        }

        System.out.println("\nNo allocation done yet (read-only queue stage).");
        System.out.println("Application terminated.");
    }
}