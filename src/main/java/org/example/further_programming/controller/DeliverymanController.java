package org.example.further_programming.controller;

import org.example.further_programming.database.Database;
import org.example.further_programming.model.Deliveryman;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliverymanController {

    public void addDeliveryman(Deliveryman d) {
        String sql = "INSERT INTO deliverymen(id, name, phone) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getId());
            stmt.setString(2, d.getName());
            stmt.setString(3, d.getPhone());
            stmt.executeUpdate();
            System.out.println("✅ Deliveryman added: " + d.getName());
        } catch (Exception e) {
            System.out.println("❌ Add Deliveryman Error: " + e.getMessage());
        }
    }

    public void updateDeliveryman(Deliveryman d) {
        String sql = "UPDATE deliverymen SET name = ?, phone = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getName());
            stmt.setString(2, d.getPhone());
            stmt.setInt(3, d.getId());
            stmt.executeUpdate();
            System.out.println("✅ Deliveryman updated: " + d.getName());
        } catch (Exception e) {
            System.out.println("❌ Update Deliveryman Error: " + e.getMessage());
        }
    }

    public void deleteDeliveryman(int id) {
        String sql = "DELETE FROM deliverymen WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Deliveryman deleted: ID " + id);
        } catch (Exception e) {
            System.out.println("❌ Delete Deliveryman Error: " + e.getMessage());
        }
    }

    public List<Deliveryman> search(String keyword, String field, boolean ascending) {
        List<Deliveryman> list = new ArrayList<>();
        String orderBy = ascending ? "ASC" : "DESC";

        String baseSQL = "SELECT * FROM deliverymen";
        String sql;

        try (Connection conn = Database.getConnection()) {
            PreparedStatement stmt;

            if (keyword == null || keyword.trim().isEmpty()) {
                sql = baseSQL + " ORDER BY " + field + " " + orderBy;
                stmt = conn.prepareStatement(sql);
            } else if (field.equalsIgnoreCase("name") || field.equalsIgnoreCase("phone")) {
                sql = baseSQL + " WHERE " + field + " LIKE ? ORDER BY " + field + " " + orderBy;
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + keyword + "%");
            } else {
                System.out.println("❌ Invalid search field: " + field);
                return list;
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Deliveryman(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Search Deliverymen Error: " + e.getMessage());
        }

        return list;
    }

    public List<Deliveryman> getAll() {
        return search("", "name", true);
    }
}
