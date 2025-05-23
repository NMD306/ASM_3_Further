package org.example.further_programming.model;

public class OrderItem {
    private int orderId;
    private int itemId;
    private String itemName;
    private int quantity;

    public OrderItem(int orderId, int itemId, String itemName, int quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    // Alternative constructor using Item object
    public OrderItem(int orderId, Item item, int quantity) {
        this.orderId = orderId;
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return itemName + " x" + quantity;
    }
}
