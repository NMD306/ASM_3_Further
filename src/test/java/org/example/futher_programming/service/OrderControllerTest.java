package org.example.futher_programming.service;

import org.example.further_programming.controller.CustomerController;
import org.example.further_programming.controller.DeliverymanController;
import org.example.further_programming.controller.OrderController;
import org.example.further_programming.database.Database;
import org.example.further_programming.model.Customer;
import org.example.further_programming.model.Deliveryman;
import org.example.further_programming.model.Order;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerTest {

    static CustomerController customerController;
    static DeliverymanController deliverymanController;
    static OrderController orderController;

    static Customer customer;
    static Deliveryman deliveryman;

    @BeforeAll
    static void setup() throws Exception {
        customerController = new CustomerController();
        deliverymanController = new DeliverymanController();
        orderController = new OrderController();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM orders");
            stmt.execute("DELETE FROM customers");
            stmt.execute("DELETE FROM deliverymen");
        }

        customer = new Customer(10, "Charlie", "789 Cherry Ave", "5551231234");
        deliveryman = new Deliveryman(10, "David", "3333333333");

        customerController.addCustomer(customer);
        deliverymanController.addDeliveryman(deliveryman);
    }

    @Test
//    @Order(1)
    void testAddOrder() {
        Order order = new Order(1001, 99.99, LocalDate.now(), customer, deliveryman);
        orderController.addOrder(order);

        List<Order> result = orderController.search("1001", "id", true);
        assertEquals(1, result.size());
        assertEquals(99.99, result.get(0).getTotalPrice());
    }

    @Test
//    @Order(2)
    void testUpdateOrder() {
        Order original = new Order(1002, 88.88, LocalDate.now(), customer, deliveryman);
        orderController.addOrder(original);

        Order updatedOrder = new Order(1002, 129.99, LocalDate.now(), customer, deliveryman);
        orderController.updateOrder(updatedOrder);

        List<Order> result = orderController.search("1002", "id", true);
        assertFalse(result.isEmpty());
        assertEquals(129.99, result.get(0).getTotalPrice());
    }

    @Test
//    @Order(3)
    void testSearchByDate() {
        LocalDate date = LocalDate.now();
        Order order = new Order(1003, 49.99, date, customer, deliveryman);
        orderController.addOrder(order);

        List<Order> result = orderController.search(date.toString(), "date", false);
        assertFalse(result.isEmpty());
    }

    @Test
//    @Order(4)
    void testDeleteOrder() {
        Order order = new Order(1004, 39.99, LocalDate.now(), customer, deliveryman);
        orderController.addOrder(order);

        orderController.deleteOrder(1004);
        List<Order> result = orderController.search("1004", "id", true);
        assertTrue(result.isEmpty());
    }
}
