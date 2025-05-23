package org.example.futher_programming.service;

import org.example.further_programming.controller.ItemController;
import org.example.further_programming.database.Database;
import org.example.further_programming.model.Item;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemControllerTest {

    static ItemController controller;

    @BeforeAll
    static void setup() {
        controller = new ItemController();
    }

    @BeforeEach
    void clearTable() throws Exception {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM items");
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
    void testAddAndGetItems() {
        controller.addItem(new Item(8, "Burger", 5.99));
        controller.addItem(new Item(9, "Pizza", 8.49));

        List<Item> items = controller.getAll();
        assertEquals(2, items.size());
    }

    @Test
    @Order(3)
    void testUpdateItem() {
        controller.addItem(new Item(10, "Pasta", 6.75));
        controller.updateItem(new Item(10, "Creamy Pasta", 7.25));

        List<Item> result = controller.search("Creamy Pasta", "name", true);
        assertFalse(result.isEmpty());
        assertEquals(7.25, result.get(0).getPrice());
    }

    @Test
    @Order(4)
    void testSearchByPriceDesc() {
        controller.addItem(new Item(11, "Fries", 5.99));
        List<Item> result = controller.search("5.99", "price", false);
        assertFalse(result.isEmpty());
        assertEquals("Fries", result.get(0).getName());
    }

    @Test
    @Order(5)
    void testDeleteItem() {
        controller.addItem(new Item(12, "Wings", 9.99));
        controller.deleteItem(12);

        List<Item> result = controller.search("Wings", "name", true);
        assertTrue(result.isEmpty());
    }
}
