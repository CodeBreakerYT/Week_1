import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryManager {

    // productId -> stock
    private ConcurrentHashMap<String, AtomicInteger> inventory = new ConcurrentHashMap<>();

    // productId -> waiting list (FIFO)
    private ConcurrentHashMap<String, LinkedHashMap<Integer, Integer>> waitingList = new ConcurrentHashMap<>();

    // Add product to inventory
    public void addProduct(String productId, int stock) {
        inventory.put(productId, new AtomicInteger(stock));
        waitingList.put(productId, new LinkedHashMap<>());
    }

    // Check stock availability
    public int checkStock(String productId) {
        AtomicInteger stock = inventory.get(productId);
        if (stock == null) return 0;
        return stock.get();
    }

    // Purchase item
    public synchronized String purchaseItem(String productId, int userId) {

        AtomicInteger stock = inventory.get(productId);

        if (stock == null) {
            return "Product not found";
        }

        // If stock available
        if (stock.get() > 0) {
            int remaining = stock.decrementAndGet();
            return "Success, " + remaining + " units remaining";
        }

        // Add to waiting list
        LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);
        int position = queue.size() + 1;
        queue.put(userId, position);

        return "Added to waiting list, position #" + position;
    }

    // Show waiting list
    public void showWaitingList(String productId) {
        LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);

        for (Map.Entry<Integer, Integer> entry : queue.entrySet()) {
            System.out.println("User " + entry.getKey() +
                    " -> Position " + entry.getValue());
        }
    }

    public static void main(String[] args) {

        InventoryManager system = new InventoryManager();

        system.addProduct("IPHONE15_256GB", 100);

        System.out.println("Stock: " + system.checkStock("IPHONE15_256GB"));

        System.out.println(system.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(system.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate stock exhaustion
        for (int i = 0; i < 100; i++) {
            system.purchaseItem("IPHONE15_256GB", 10000 + i);
        }

        System.out.println(system.purchaseItem("IPHONE15_256GB", 99999));

        system.showWaitingList("IPHONE15_256GB");
    }
}