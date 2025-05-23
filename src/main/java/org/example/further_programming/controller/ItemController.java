package org.example.further_programming.controller;

import org.example.further_programming.database.Database;
import org.example.further_programming.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemController {

    public void addItem(Item item) {
        String sql = "INSERT INTO items(id, name, price) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getId());
            stmt.setString(2, item.getName());
            stmt.setDouble(3, item.getPrice());
            stmt.executeUpdate();
            System.out.println("✅ Item added: " + item.getName());

        } catch (SQLException e) {
            System.out.println("❌ Add item failed: " + e.getMessage());
        }
    }

    public void updateItem(Item item) {
        String sql = "UPDATE items SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setInt(3, item.getId());
            stmt.executeUpdate();
            System.out.println("✅ Item updated: " + item.getName());

        } catch (SQLException e) {
            System.out.println("❌ Update item failed: " + e.getMessage());
        }
    }

    public void deleteItem(int id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Item deleted: ID " + id);

        } catch (SQLException e) {
            System.out.println("❌ Delete item failed: " + e.getMessage());
        }
    }

    public List<Item> search(String keyword, String field, boolean ascending) {
        List<Item> items = new ArrayList<>();
        String order = ascending ? "ASC" : "DESC";

        String sql = "SELECT * FROM items ";
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = createSearchStatement(conn, field, keyword, hasKeyword, order)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }

        } catch (SQLException e) {
            System.out.println("❌ Item search failed: " + e.getMessage());
        }

        return items;
    }

    private PreparedStatement createSearchStatement(Connection conn, String field, String keyword, boolean hasKeyword, String order) throws SQLException {
        String sql;
        PreparedStatement stmt;

        if (!hasKeyword) {
            sql = "SELECT * FROM items ORDER BY " + field + " " + order;
            stmt = conn.prepareStatement(sql);
        } else if (field.equalsIgnoreCase("name")) {
            sql = "SELECT * FROM items WHERE name LIKE ? ORDER BY name " + order;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
        } else if (field.equalsIgnoreCase("price")) {
            sql = "SELECT * FROM items WHERE price = ? ORDER BY price " + order;
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, Double.parseDouble(keyword.trim()));
        } else {
            throw new SQLException("Invalid search field: " + field);
        }

        return stmt;
    }

    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Get all items failed: " + e.getMessage());
        }

        return items;
    }

}
