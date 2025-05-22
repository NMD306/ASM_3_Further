package org.example.further_programming.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private double totalPrice;
    private LocalDate date;

    private Customer customer; // Bi-directional
    private Deliveryman deliveryman; // Bi-directional

    private List<Item> items = new ArrayList<>(); // Unidirectional

    public Order(int id, double totalPrice, LocalDate date, Customer customer, Deliveryman deliveryman) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.date = date;
        this.customer = customer;
        this.deliveryman = deliveryman;
    }

    public int getId() { return id; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDate getDate() { return date; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Deliveryman getDeliveryman() { return deliveryman; }
    public void setDeliveryman(Deliveryman deliveryman) { this.deliveryman = deliveryman; }

    public List<Item> getItems() { return items; }
    public void addItem(Item item) {
        items.add(item);
    }
}
