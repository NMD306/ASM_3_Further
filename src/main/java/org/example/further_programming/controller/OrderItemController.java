package org.example.further_programming.controller;

import org.example.further_programming.database.Database;
import org.example.further_programming.model.Item;
import org.example.further_programming.model.OrderItem;

import java.sql.*;
import java.util.*;

public class OrderItemController {

    // ✅ Fetch all order item records with joined item name
    public List<OrderItem> getAll() {
        List<OrderItem> list = new ArrayList<>();
        String sql = """
            SELECT oi.order_id, oi.item_id, i.name AS item_name, oi.quantity
            FROM order_items oi
            JOIN items i ON oi.item_id = i.id
        """;

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Item item = new Item(rs.getInt("item_id"), rs.getString("item_name"), 0); // price not used here
                list.add(new OrderItem(
                        rs.getInt("order_id"),
                        item,
                        rs.getInt("quantity")
                ));
            }
        } catch (Exception e) {
            System.out.println("❌ Fetch order items failed: " + e.getMessage());
        }
        return list;
    }

    // ✅ Insert a single order-item record
    public void addOrderItem(int orderId, int itemId, int quantity) {
        String sql = "INSERT INTO order_items(order_id, item_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.setInt(2, itemId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();

            System.out.println("✅ Added order item → Order: " + orderId + ", Item: " + itemId + ", Qty: " + quantity);

        } catch (Exception e) {
            System.out.println("❌ Add order item failed: " + e.getMessage());
        }
    }

    // ✅ Batch insert: map of item and quantity
    public void addItemsForOrder(int orderId, Map<Item, Integer> items) {
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            addOrderItem(orderId, entry.getKey().getId(), entry.getValue());
        }
    }

    // ✅ Remove all items for a specific order
    public void deleteByOrderId(int orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            System.out.println("🗑️ Cleared order items for Order ID: " + orderId);

        } catch (Exception e) {
            System.out.println("❌ Delete order items failed: " + e.getMessage());
        }
    }
}
