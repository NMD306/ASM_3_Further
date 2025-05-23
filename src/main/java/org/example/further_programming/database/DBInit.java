package org.example.further_programming.database;

import java.sql.Connection;
import java.sql.Statement;

public class DBInit {
    public static void init() {
        String dropTables = """
            DROP TABLE IF EXISTS order_items;
            DROP TABLE IF EXISTS orders;
            DROP TABLE IF EXISTS deliverymen;
            DROP TABLE IF EXISTS customers;
            DROP TABLE IF EXISTS items;
        """;

        String createItemsTable = """
            CREATE TABLE IF NOT EXISTS items (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                price REAL NOT NULL
            );
        """;

        String createCustomerTable = """
            CREATE TABLE IF NOT EXISTS customers (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                address TEXT NOT NULL,
                phone TEXT NOT NULL
            );
        """;

        String createDeliverymanTable = """
            CREATE TABLE IF NOT EXISTS deliverymen (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                phone TEXT NOT NULL
            );
        """;

        String createOrdersTable = """
            CREATE TABLE IF NOT EXISTS orders (
                id INTEGER PRIMARY KEY,
                total_price REAL NOT NULL,
                date TEXT NOT NULL,
                customer_id INTEGER NOT NULL,
                deliveryman_id INTEGER NOT NULL,
                FOREIGN KEY (customer_id) REFERENCES customers(id),
                FOREIGN KEY (deliveryman_id) REFERENCES deliverymen(id)
            );
        """;

        String createOrderItemsTable = """
            CREATE TABLE IF NOT EXISTS order_items (
                order_id INTEGER NOT NULL,
                item_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                PRIMARY KEY (order_id, item_id),
                FOREIGN KEY (order_id) REFERENCES orders(id),
                FOREIGN KEY (item_id) REFERENCES items(id)
            );
        """;

        String insertItems = """
            INSERT INTO items (id, name, price) VALUES
            (1, 'Cheese Burger', 10.25),
            (2, 'Pizza', 12.99);
        """;

        String insertCustomers = """
            INSERT INTO customers (id, name, address, phone) VALUES
            (1, 'Alice', '123 Elm St', '0123456789'),
            (2, 'Bob', '456 Oak Ave', '0987654321');
        """;

        String insertDeliverymen = """
            INSERT INTO deliverymen (id, name, phone) VALUES
            (1, 'Derek', '0991112222'),
            (2, 'Eva', '0883334444');
        """;

        String insertOrders = """
            INSERT INTO orders (id, total_price, date, customer_id, deliveryman_id) VALUES
            (1, 22.50, '2024-05-01', 1, 1),
            (2, 35.75, '2024-05-02', 2, 2);
        """;

        String insertOrderItems = """
            INSERT INTO order_items (order_id, item_id, quantity) VALUES
            (1, 1, 2),  -- Order 1: Pizza x2
            (1, 2, 1),  -- Order 1: Cheese Burger x1
            (2, 1, 3);  -- Order 2: Pizza x3
        """;

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {
//            stmt.execute(dropTables);
            stmt.execute(createItemsTable);
            stmt.execute(createCustomerTable);
            stmt.execute(createDeliverymanTable);
            stmt.execute(createOrdersTable);
            stmt.execute(createOrderItemsTable);

//            stmt.execute(insertItems);
//            stmt.execute(insertCustomers);
//            stmt.execute(insertDeliverymen);
//            stmt.execute(insertOrders);
//            stmt.execute(insertOrderItems);

            System.out.println("✅ All tables created and sample data inserted.");
        } catch (Exception e) {
            System.out.println("❌ DB init error: " + e.getMessage());
        }
    }
}
