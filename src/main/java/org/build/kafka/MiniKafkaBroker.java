package org.build.kafka;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class MiniKafkaBroker {


    // 1. THE STORAGE (The Disk)
    // Key: Topic Name (e.g., "order-events")
    // Value: The Log (A list of messages)
    private Map<String, List<String>> topics = new ConcurrentHashMap<>();


    // 2. THE OFFSETS (The Bookmarks)
    // Key: Group ID (e.g., "shipping-team")
    // Value: A Map of Topic -> Last Read Index
    private Map<String, Map<String, Integer>> consumerOffsets = new ConcurrentHashMap<>();


    // --- PRODUCER LOGIC ---


    public void publish(String topic, String message) {
        // Create topic if it doesn't exist
        topics.putIfAbsent(topic, new ArrayList<>());


        // Append to the end of the list (O(1) operation)
        List<String> log = topics.get(topic);
        synchronized (log) {
            log.add(message);
            int newOffset = log.size() - 1;
            System.out.println("[Producer] Appended to " + topic + " @ Offset " + newOffset + ": " + message);
        }
    }


    // --- CONSUMER LOGIC ---


    public String consume(String topic, String groupId) {
        // Ensure topic exists
        if (!topics.containsKey(topic)) return null;


        // 1. Find where this group left off (Get the Bookmark)
        consumerOffsets.putIfAbsent(groupId, new HashMap<>());
        Map<String, Integer> groupOffsets = consumerOffsets.get(groupId);


        // Default offset is 0 if they are new
        int nextOffset = groupOffsets.getOrDefault(topic, 0);


        // 2. Check if there is a message at that offset
        List<String> log = topics.get(topic);
        if (nextOffset >= log.size()) {
            return null; // No new messages (We are at the end)
        }


        // 3. Read the message (O(1) Access)
        String message = log.get(nextOffset);


        // 4. Move the Bookmark forward (Commit)
        groupOffsets.put(topic, nextOffset + 1);


        System.out.println("  [Consumer: " + groupId + "] Read: " + message + " (Next Offset: " + (nextOffset + 1) + ")");
        return message;
    }


    // --- MAIN METHOD TO TEST ---


    public static void main(String[] args) {
        MiniKafkaBroker broker = new MiniKafkaBroker();


        // 1. Producer publishes 3 messages
        System.out.println("--- PRODUCING ---");
        broker.publish("orders", "Order #1");
        broker.publish("orders", "Order #2");
        broker.publish("orders", "Order #3");


        // 2. Shipping Team reads them all
        System.out.println("\n--- SHIPPING TEAM CONSUMING ---");
        broker.consume("orders", "shipping-team"); // Reads Order #1
        broker.consume("orders", "shipping-team"); // Reads Order #2


        // 3. Analytics Team comes in late (New Group)
        // They start from the beginning (Offset 0) because they have their own bookmark!
        System.out.println("\n--- ANALYTICS TEAM CONSUMING (Independent) ---");
        broker.consume("orders", "analytics-team"); // Reads Order #1
        broker.consume("orders", "analytics-team"); // Reads Order #2
        broker.consume("orders", "analytics-team"); // Reads Order #3


        // 4. Shipping Team comes back
        // They remember they were at Offset 2, so they read Order #3
        System.out.println("\n--- SHIPPING TEAM RETURNS ---");
        broker.consume("orders", "shipping-team"); // Reads Order #3
    }
}


