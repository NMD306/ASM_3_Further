package org.example.further_programming.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.further_programming.controller.CustomerController;
import org.example.further_programming.model.Customer;

public class CustomerView {

    private static final CustomerController controller = new CustomerController();
    private static final ObservableList<Customer> data = FXCollections.observableArrayList();

    public static VBox create() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label title = new Label("👤 Customer Management");

        TableView<Customer> table = new TableView<>(data);
        table.setPrefHeight(300);

        TableColumn<Customer, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));

        TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAddress()));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));

        table.getColumns().addAll(idCol, nameCol, addressCol, phoneCol);

        // Form fields
        TextField idField = new TextField(); idField.setPromptText("ID");
        TextField nameField = new TextField(); nameField.setPromptText("Name");
        TextField addressField = new TextField(); addressField.setPromptText("Address");
        TextField phoneField = new TextField(); phoneField.setPromptText("Phone");

        // Buttons
        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button refreshBtn = new Button("Refresh");

        // Search
        TextField searchField = new TextField(); searchField.setPromptText("Search...");
        ComboBox<String> searchType = new ComboBox<>(FXCollections.observableArrayList("name", "phone"));
        searchType.setValue("name");

        ComboBox<String> orderBox = new ComboBox<>(FXCollections.observableArrayList("Ascending", "Descending"));
        orderBox.setValue("Ascending");

        HBox formRow = new HBox(10, idField, nameField, addressField, phoneField);
        HBox buttonRow = new HBox(10, addBtn, updateBtn, refreshBtn);
        HBox searchRow = new HBox(10, new Label("Search:"), searchField, searchType, orderBox);

        // Events
        addBtn.setOnAction(e -> {
            try {
                Customer c = new Customer(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        addressField.getText(),
                        phoneField.getText()
                );
                controller.addCustomer(c);
                clear(idField, nameField, addressField, phoneField);
                refresh(data, "", "name", "Ascending");
            } catch (Exception ex) {
                show("Invalid input for add.");
            }
        });

        updateBtn.setOnAction(e -> {
            try {
                Customer c = new Customer(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        addressField.getText(),
                        phoneField.getText()
                );
                controller.updateCustomer(c);
                refresh(data, "", "name", "Ascending");
            } catch (Exception ex) {
                show("Invalid input for update.");
            }
        });

        refreshBtn.setOnAction(e -> refresh(data, "", "name", "Ascending"));

        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                refresh(data, newVal, searchType.getValue(), orderBox.getValue()));
        searchType.setOnAction(e -> refresh(data, searchField.getText(), searchType.getValue(), orderBox.getValue()));
        orderBox.setOnAction(e -> refresh(data, searchField.getText(), searchType.getValue(), orderBox.getValue()));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, selected) -> {
            if (selected != null) {
                idField.setText(String.valueOf(selected.getId()));
                nameField.setText(selected.getName());
                addressField.setText(selected.getAddress());
                phoneField.setText(selected.getPhone());
            }
        });

        layout.getChildren().addAll(title, searchRow, table, formRow, buttonRow);
        refresh(data, "", "name", "Ascending");
        return layout;
    }

    private static void refresh(ObservableList<Customer> list, String keyword, String field, String direction) {
        boolean asc = direction.equalsIgnoreCase("Ascending");
        list.clear();
        list.addAll(controller.search(keyword, field, asc));
    }

    private static void clear(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    private static void show(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
