package org.example.further_programming.controller;

import org.example.further_programming.database.Database;
import org.example.further_programming.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    // ➕ Add customer
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO customers(id, name, address, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customer.getId());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getPhone());
            stmt.executeUpdate();
            System.out.println("✅ Customer added: " + customer.getName());

        } catch (SQLException e) {
            System.out.println("❌ Add Customer Error: " + e.getMessage());
        }
    }

    // ✏️ Update customer
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET name = ?, address = ?, phone = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPhone());
            stmt.setInt(4, customer.getId());
            stmt.executeUpdate();
            System.out.println("✅ Customer updated: " + customer.getId());

        } catch (SQLException e) {
            System.out.println("❌ Update Customer Error: " + e.getMessage());
        }
    }

    // 🔍 Search customers by name or phone with sorting
    public List<Customer> search(String keyword, String field, boolean ascending) {
        List<Customer> list = new ArrayList<>();
        String order = ascending ? "ASC" : "DESC";

        String sql = """
            SELECT * FROM customers
        """;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        try (Connection conn = Database.getConnection()) {
            PreparedStatement stmt;

            if (!hasKeyword) {
                sql += " ORDER BY " + field + " " + order;
                stmt = conn.prepareStatement(sql);
            } else if (field.equalsIgnoreCase("name") || field.equalsIgnoreCase("phone")) {
                sql += " WHERE " + field + " LIKE ? ORDER BY " + field + " " + order;
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + keyword.trim() + "%");
            } else {
                System.out.println("❌ Invalid search field: " + field);
                return list;
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                ));
            }

        } catch (SQLException e) {
            System.out.println("❌ Search Customer Error: " + e.getMessage());
        }

        return list;
    }

    // 📋 Get all customers (for ComboBox, etc.)
    public List<Customer> getAll() {
        return search("", "id", true);
    }
}
