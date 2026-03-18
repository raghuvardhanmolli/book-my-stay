import java.util.*;

// Booking Request Class
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
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

// Shared Booking System
class BookingSystem {

    // Shared Inventory
    private Map<String, Integer> roomInventory = new HashMap<>();

    // Shared Queue
    private Queue<BookingRequest> bookingQueue = new LinkedList<>();

    public BookingSystem() {
        roomInventory.put("Single", 2);
        roomInventory.put("Double", 2);
    }

    // Add request (Producer)
    public synchronized void addBookingRequest(BookingRequest request) {
        bookingQueue.add(request);
        System.out.println(request.getGuestName() + " requested " + request.getRoomType() + " room");
    }

    // Process request (Consumer)
    public void processBooking() {
        while (true) {
            BookingRequest request;

            // Critical Section - Fetch request
            synchronized (this) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.poll();
            }

            // Critical Section - Allocate room
            allocateRoom(request);
        }
    }

    // Synchronized allocation to prevent race conditions
    private synchronized void allocateRoom(BookingRequest request) {
        String roomType = request.getRoomType();
        int available = roomInventory.getOrDefault(roomType, 0);

        if (available > 0) {
            roomInventory.put(roomType, available - 1);
            System.out.println("✅ Booking CONFIRMED for " + request.getGuestName() +
                    " (" + roomType + " room)");
        } else {
            System.out.println("❌ Booking FAILED for " + request.getGuestName() +
                    " (" + roomType + " room not available)");
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Room Availability:");
        for (String type : roomInventory.keySet()) {
            System.out.println(type + " rooms left: " + roomInventory.get(type));
        }
    }
}

// Thread Class
class BookingProcessor extends Thread {

    private BookingSystem system;

    public BookingProcessor(BookingSystem system) {
        this.system = system;
    }

    @Override
    public void run() {
        system.processBooking();
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Simulate multiple guest requests
        system.addBookingRequest(new BookingRequest("Guest1", "Single"));
        system.addBookingRequest(new BookingRequest("Guest2", "Single"));
        system.addBookingRequest(new BookingRequest("Guest3", "Single"));
        system.addBookingRequest(new BookingRequest("Guest4", "Double"));
        system.addBookingRequest(new BookingRequest("Guest5", "Double"));
        system.addBookingRequest(new BookingRequest("Guest6", "Double"));

        // Create multiple threads
        BookingProcessor t1 = new BookingProcessor(system);
        BookingProcessor t2 = new BookingProcessor(system);
        BookingProcessor t3 = new BookingProcessor(system);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display final inventory
        system.displayInventory();
    }
}