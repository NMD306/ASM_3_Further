package org.example.further_programming.controller;

import org.example.further_programming.database.Database;
import org.example.further_programming.model.Customer;
import org.example.further_programming.model.Deliveryman;
import org.example.further_programming.model.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public void addOrder(Order order) {
        String sql = "INSERT INTO orders(id, total_price, date, customer_id, deliveryman_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getId());
            stmt.setDouble(2, order.getTotalPrice());
            stmt.setString(3, order.getDate().toString()); // ISO string
            stmt.setInt(4, order.getCustomer().getId());
            stmt.setInt(5, order.getDeliveryman().getId());

            stmt.executeUpdate();
            System.out.println("✅ Order added: " + order);

        } catch (SQLException e) {
            System.out.println("❌ Add order failed: " + e.getMessage());
        }
    }

    public void updateOrder(Order order) {
        String sql = "UPDATE orders SET total_price = ?, date = ?, customer_id = ?, deliveryman_id = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, order.getTotalPrice());
            stmt.setString(2, order.getDate().toString());
            stmt.setInt(3, order.getCustomer().getId());
            stmt.setInt(4, order.getDeliveryman().getId());
            stmt.setInt(5, order.getId());

            stmt.executeUpdate();
            System.out.println("✅ Order updated: " + order);

        } catch (SQLException e) {
            System.out.println("❌ Update order failed: " + e.getMessage());
        }
    }

    public void deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Order deleted: ID " + id);

        } catch (SQLException e) {
            System.out.println("❌ Delete failed: " + e.getMessage());
        }
    }

    public List<Order> search(String keyword, String field, boolean ascending) {
        List<Order> orders = new ArrayList<>();
        String orderBy = ascending ? "ASC" : "DESC";

        String baseSQL = """
            SELECT o.*, 
                   c.name AS customer_name, c.address, c.phone AS customer_phone,
                   d.name AS delivery_name, d.phone AS delivery_phone
            FROM orders o
            LEFT JOIN customers c ON o.customer_id = c.id
            LEFT JOIN deliverymen d ON o.deliveryman_id = d.id
        """;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        String sql;
        PreparedStatement stmt = null;

        try (Connection conn = Database.getConnection()) {

            if (!hasKeyword) {
                sql = baseSQL + " ORDER BY o." + field + " " + orderBy;
                stmt = conn.prepareStatement(sql);
            } else if (field.equalsIgnoreCase("id")) {
                sql = baseSQL + " WHERE o.id = ? ORDER BY o.id " + orderBy;
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(keyword.trim()));
            } else if (field.equalsIgnoreCase("date")) {
                sql = baseSQL + " WHERE o.date = ? ORDER BY o.date " + orderBy;
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, keyword.trim());
            } else {
                System.out.println("❌ Invalid search field: " + field);
                return orders;
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("customer_phone")
                );
                Deliveryman deliveryman = new Deliveryman(
                        rs.getInt("deliveryman_id"),
                        rs.getString("delivery_name"),
                        rs.getString("delivery_phone")
                );
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getDouble("total_price"),
                        LocalDate.parse(rs.getString("date")),
                        customer,
                        deliveryman
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Search failed: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) {}
        }

        return orders;
    }
}
