package SocialMedia;

import java.util.*;

public class socialMedia {

    // Stores username -> userId
    private HashMap<String, Integer> users = new HashMap<>();

    // Stores username -> attempt count
    private HashMap<String, Integer> attempts = new HashMap<>();

    // Constructor with some sample users
    public socialMedia() {
        users.put("john_doe", 1);
        users.put("admin", 2);
        users.put("player1", 3);
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        // O(1) lookup
        return !users.containsKey(username);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        // Add numbers
        for (int i = 1; i <= 3; i++) {
            String newName = username + i;
            if (!users.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        // Replace underscore with dot
        if (username.contains("_")) {
            String alt = username.replace("_", ".");
            if (!users.containsKey(alt)) {
                suggestions.add(alt);
            }
        }

        return suggestions;
    }

    // Find most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attempts.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }

    // Register new user
    public void registerUser(String username, int userId) {
        users.put(username, userId);
    }

    public static void main(String[] args) {

        socialMedia system = new socialMedia();

        System.out.println(system.checkAvailability("john_doe"));
        System.out.println(system.checkAvailability("jane_smith"));

        System.out.println(system.suggestAlternatives("john_doe"));

        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        System.out.println(system.getMostAttempted());
    }
}