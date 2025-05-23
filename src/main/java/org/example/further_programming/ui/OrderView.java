package org.example.further_programming.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.further_programming.controller.*;
import org.example.further_programming.model.*;

import java.time.LocalDate;
import java.util.*;

public class OrderView {

    private static final OrderController controller = new OrderController();
    private static final ObservableList<Order> data = FXCollections.observableArrayList();

    public static VBox create() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        Label title = new Label("📦 Order Management");

        TableView<Order> table = new TableView<>(data);
        table.setPrefHeight(300);

        // Table columns
        TableColumn<Order, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(o -> new javafx.beans.property.SimpleIntegerProperty(o.getValue().getId()).asObject());

        TableColumn<Order, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(o -> new javafx.beans.property.SimpleDoubleProperty(o.getValue().getTotalPrice()).asObject());

        TableColumn<Order, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(o -> new javafx.beans.property.SimpleStringProperty(o.getValue().getDate().toString()));

        TableColumn<Order, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(o -> new javafx.beans.property.SimpleStringProperty(o.getValue().getCustomer().getName()));

        TableColumn<Order, String> deliveryCol = new TableColumn<>("Deliveryman");
        deliveryCol.setCellValueFactory(o -> new javafx.beans.property.SimpleStringProperty(o.getValue().getDeliveryman().getName()));

        table.getColumns().addAll(idCol, priceCol, dateCol, customerCol, deliveryCol);

        // Form fields
        TextField idField = new TextField(); idField.setPromptText("ID");
        TextField dateField = new TextField(); dateField.setPromptText("YYYY-MM-DD");

        ComboBox<Customer> customerBox = new ComboBox<>(FXCollections.observableArrayList(new CustomerController().getAll()));
        customerBox.setPromptText("Customer");

        ComboBox<Deliveryman> deliveryBox = new ComboBox<>(FXCollections.observableArrayList(new DeliverymanController().getAll()));
        deliveryBox.setPromptText("Deliveryman");

        ComboBox<Item> itemBox = new ComboBox<>(FXCollections.observableArrayList(new ItemController().getAll()));
        itemBox.setPromptText("Item");

        TextField qtyField = new TextField(); qtyField.setPromptText("Qty");

        Button addItemBtn = new Button("+ Add Item");

        Map<Item, Integer> selectedItems = new HashMap<>();
        ObservableList<String> itemSummaries = FXCollections.observableArrayList();
        ListView<String> itemListView = new ListView<>(itemSummaries);

        // Buttons
        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button refreshBtn = new Button("Refresh");

        // Search controls
        TextField searchField = new TextField(); searchField.setPromptText("Search...");
        ComboBox<String> searchType = new ComboBox<>(FXCollections.observableArrayList("id", "date")); searchType.setValue("id");
        ComboBox<String> orderBox = new ComboBox<>(FXCollections.observableArrayList("Ascending", "Descending")); orderBox.setValue("Ascending");

        // Add item button
        addItemBtn.setOnAction(e -> {
            Item selectedItem = itemBox.getValue();
            String qtyStr = qtyField.getText().trim();
            if (selectedItem == null || qtyStr.isEmpty()) {
                showWarning("Please select an item and enter quantity.");
                return;
            }
            if (selectedItems.containsKey(selectedItem)) {
                showWarning("This item is already added.");
                return;
            }

            try {
                int qty = Integer.parseInt(qtyStr);
                if (qty <= 0) {
                    showWarning("Quantity must be positive.");
                    return;
                }
                selectedItems.put(selectedItem, qty);
                itemSummaries.add(selectedItem.getName() + " x " + qty);
                qtyField.clear();
                itemBox.setValue(null);
            } catch (NumberFormatException ex) {
                showWarning("Quantity must be a number.");
            }
        });

        // Add order
        addBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                Customer customer = customerBox.getValue();
                Deliveryman deliveryman = deliveryBox.getValue();

                if (customer == null || deliveryman == null) {
                    showWarning("Please select customer and deliveryman.");
                    return;
                }
                if (selectedItems.isEmpty()) {
                    showWarning("Please add at least one item.");
                    return;
                }

                double total = selectedItems.entrySet().stream()
                        .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                        .sum();

                Order order = new Order(id, total, date, customer, deliveryman);
                selectedItems.forEach((item, qty) -> {
                    order.addItem(new OrderItem(order.getId(), item, qty));
                });


                controller.addOrder(order);
                new OrderItemController().addItemsForOrder(id, selectedItems);
                clearForm(idField, dateField, customerBox, deliveryBox, itemBox, qtyField, itemSummaries, selectedItems);
                refresh(data, "", "id", "Ascending");

            } catch (Exception ex) {
                showWarning("Add failed: " + ex.getMessage());
            }
        });

        updateBtn.setOnAction(e -> showWarning("Update logic is not supported in this demo."));
        deleteBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                controller.deleteOrder(id);
                refresh(data, "", "id", "Ascending");
            } catch (Exception ex) {
                showWarning("Delete failed: " + ex.getMessage());
            }
        });

        refreshBtn.setOnAction(e -> refresh(data, "", "id", "Ascending"));
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                refresh(data, newVal.trim(), searchType.getValue(), orderBox.getValue()));
        searchType.setOnAction(e -> refresh(data, searchField.getText().trim(), searchType.getValue(), orderBox.getValue()));
        orderBox.setOnAction(e -> refresh(data, searchField.getText().trim(), searchType.getValue(), orderBox.getValue()));

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                idField.setText(String.valueOf(selected.getId()));
                dateField.setText(selected.getDate().toString());
                customerBox.setValue(selected.getCustomer());
                deliveryBox.setValue(selected.getDeliveryman());
                itemSummaries.clear();
            }
        });

        layout.getChildren().addAll(title,
                new HBox(10, new Label("Search:"), searchField, searchType, orderBox),
                table,
                new HBox(10, idField, dateField, customerBox, deliveryBox),
                new HBox(10, itemBox, qtyField, addItemBtn),
                itemListView,
                new HBox(10, addBtn, updateBtn, deleteBtn, refreshBtn)
        );

        refresh(data, "", "id", "Ascending");
        return layout;
    }

    private static void refresh(ObservableList<Order> list, String keyword, String field, String direction) {
        boolean asc = direction.equalsIgnoreCase("Ascending");
        list.clear();
        list.addAll(new OrderController().search(keyword, field, asc));
    }

    private static void clearForm(TextField id, TextField date, ComboBox<Customer> cust,
                                  ComboBox<Deliveryman> deliv, ComboBox<Item> itemBox, TextField qty,
                                  ObservableList<String> itemsList, Map<Item, Integer> selectedItems) {
        id.clear(); date.clear(); qty.clear();
        cust.setValue(null); deliv.setValue(null); itemBox.setValue(null);
        itemsList.clear(); selectedItems.clear();
    }

    private static void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("⚠️ Warning");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
