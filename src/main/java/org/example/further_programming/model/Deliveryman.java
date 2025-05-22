package org.example.further_programming.model;

import java.util.ArrayList;
import java.util.List;

public class Deliveryman {
    private int id;
    private String name;
    private String phone;
    private List<Order> orders = new ArrayList<>(); // bi-directional

    public Deliveryman(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    public List<Order> getOrders() { return orders; }
    public void addOrder(Order order) {
        orders.add(order);
        order.setDeliveryman(this); // maintain bi-directional link
    }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
