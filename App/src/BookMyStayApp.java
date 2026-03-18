import java.io.*;
import java.util.*;

// Booking Request (Serializable)
class BookingRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String guestName;
    private String roomType;

    public BookingRecord(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return guestName + " -> " + roomType;
    }
}

// System State (Serializable)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<BookingRecord> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<BookingRecord> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Booking System
class BookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();
    private List<BookingRecord> bookingHistory = new ArrayList<>();

    private final String FILE_NAME = "system_state.dat";

    public BookingSystem() {
        loadState(); // Try to restore state
    }

    // Booking Logic
    public void bookRoom(String guestName, String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            bookingHistory.add(new BookingRecord(guestName, roomType));
            System.out.println("✅ Booking confirmed for " + guestName);
        } else {
            System.out.println("❌ No " + roomType + " rooms available for " + guestName);
        }
    }

    // Initialize default inventory
    private void initializeDefault() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        System.out.println("⚠️ Starting with default inventory");
    }

    // Save state to file
    public void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            SystemState state = new SystemState(inventory, bookingHistory);
            oos.writeObject(state);
            System.out.println("💾 System state saved successfully.");
        } catch (IOException e) {
            System.out.println("⚠️ Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    @SuppressWarnings("unchecked")
    private void loadState() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("⚠️ No previous data found.");
            initializeDefault();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) ois.readObject();
            this.inventory = state.inventory;
            this.bookingHistory = state.bookingHistory;

            System.out.println("✅ System state restored successfully.");
        } catch (Exception e) {
            System.out.println("⚠️ Corrupted data. Starting fresh.");
            initializeDefault();
        }
    }

    // Display Data
    public void displayStatus() {
        System.out.println("\n📊 Current Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }

        System.out.println("\n📜 Booking History:");
        if (bookingHistory.isEmpty()) {
            System.out.println("No bookings yet.");
        } else {
            for (BookingRecord record : bookingHistory) {
                System.out.println(record);
            }
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Simulate bookings
        system.bookRoom("Guest1", "Single");
        system.bookRoom("Guest2", "Double");
        system.bookRoom("Guest3", "Single");

        system.displayStatus();

        // Save state before shutdown
        system.saveState();

        System.out.println("\n🔄 Restart the application to see recovery...");
    }
}