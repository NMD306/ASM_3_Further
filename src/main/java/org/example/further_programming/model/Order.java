package org.example.further_programming.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private double totalPrice;
    private LocalDate date;
    private Customer customer;
    private Deliveryman deliveryman;
    private List<OrderItem> items = new ArrayList<>();

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

    public List<OrderItem> getItems() { return items; }

    /**
     * Adds a new item to the order. Each item can only appear once.
     * If item already exists, it won’t be added again.
     */
    public void addItem(OrderItem newItem) {
        for (OrderItem existing : items) {
            if (existing.getItemId() == newItem.getItemId()) {
                System.out.println("⚠️ Item already exists in this order: " + newItem.getItemName());
                return;
            }
        }
        items.add(newItem);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", date=" + date +
                ", customer=" + customer.getName() +
                ", deliveryman=" + deliveryman.getName() +
                ", items=" + items.size() +
                '}';
    }
}
