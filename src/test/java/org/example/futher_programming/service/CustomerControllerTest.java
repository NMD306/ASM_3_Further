package org.example.futher_programming.service;

import org.example.further_programming.controller.CustomerController;
import org.example.further_programming.database.Database;
import org.example.further_programming.model.Customer;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerTest {

    static CustomerController controller;

    @BeforeAll
    static void init() {
        controller = new CustomerController();
    }

    @BeforeEach
    void clearCustomerTable() throws Exception {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM customers");
        }
    }

    @Test
    @Order(1)
    void testDatabaseConnection() {
        assertDoesNotThrow(() -> {
            try (Connection conn = Database.getConnection()) {
                assertNotNull(conn);
                System.out.println("✅ Connected to SQLite DB: " + conn.getMetaData().getURL());
            }
        });
    }

    @Test
    @Order(2)
    void testAddAndGetCustomers() {
        Customer c1 = new Customer(8, "Alice", "123 Apple St", "1234567890");
        Customer c2 = new Customer(9, "Bob", "456 Banana Rd", "9876543210");

        controller.addCustomer(c1);
        controller.addCustomer(c2);

        List<Customer> customers = controller.getAll();
        assertEquals(2, customers.size());
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        Customer c = new Customer(8, "Alice", "123 Apple St", "1234567890");
        controller.addCustomer(c);

        c.setName("Alicia");
        controller.updateCustomer(c);

        List<Customer> updated = controller.search("Alicia", "name", true);
        assertFalse(updated.isEmpty());
        assertEquals("Alicia", updated.get(0).getName());
    }

    @Test
    @Order(4)
    void testSearchByPhoneDescending() {
        controller.addCustomer(new Customer(10, "Charlie", "789 Cherry Ave", "5551231234"));
        controller.addCustomer(new Customer(11, "David", "456 Banana Rd", "9876543210"));

        List<Customer> results = controller.search("9876543210", "phone", false);
        assertFalse(results.isEmpty());
        assertEquals("David", results.get(0).getName());
    }
}
