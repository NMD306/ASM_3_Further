package org.example.further_programming.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String phone;
    private List<Order> orders = new ArrayList<>(); // bi-directional

    public Customer(int id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }

    public List<Order> getOrders() { return orders; }
    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this); // maintain bi-directional link
    }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
