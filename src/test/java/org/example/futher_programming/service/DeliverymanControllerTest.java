package org.example.futher_programming.service;

import org.example.further_programming.controller.DeliverymanController;
import org.example.further_programming.database.Database;
import org.example.further_programming.model.Deliveryman;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeliverymanControllerTest {

    static DeliverymanController controller;

    @BeforeAll
    static void setup() {
        controller = new DeliverymanController();
    }

    @BeforeEach
    void clearTable() throws Exception {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM deliverymen");
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
    void testAddDeliverymen() {
        controller.addDeliveryman(new Deliveryman(8, "John", "1111111111"));
        controller.addDeliveryman(new Deliveryman(9, "Mike", "2222222222"));

        List<Deliveryman> all = controller.getAll();
        assertEquals(2, all.size());
    }

    @Test
    @Order(3)
    void testUpdateDeliveryman() {
        controller.addDeliveryman(new Deliveryman(9, "Mike", "2222222222"));
        controller.updateDeliveryman(new Deliveryman(9, "Michael", "2222222222"));

        List<Deliveryman> result = controller.search("Michael", "name", true);
        assertFalse(result.isEmpty());
        assertEquals("Michael", result.get(0).getName());
    }

    @Test
    @Order(4)
    void testSearchByPhone() {
        controller.addDeliveryman(new Deliveryman(10, "David", "3333333333"));
        List<Deliveryman> results = controller.search("3333333333", "phone", false);
        assertEquals("David", results.get(0).getName());
    }

    @Test
    @Order(5)
    void testDeleteDeliveryman() {
        controller.addDeliveryman(new Deliveryman(11, "Temporary", "0000000000"));
        controller.deleteDeliveryman(11);

        List<Deliveryman> result = controller.search("Temporary", "name", true);
        assertTrue(result.isEmpty());
    }
}
