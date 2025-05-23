package org.example.further_programming.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.further_programming.controller.ItemController;
import org.example.further_programming.model.Item;

public class ItemView {
    private static final ItemController controller = new ItemController();
    private static final ObservableList<Item> data = FXCollections.observableArrayList();

    public static VBox create() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label title = new Label("🍕 Item Management");

        TableView<Item> table = new TableView<>(data);
        table.setPrefHeight(300);

        TableColumn<Item, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(i -> new javafx.beans.property.SimpleIntegerProperty(i.getValue().getId()).asObject());

        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(i -> new javafx.beans.property.SimpleStringProperty(i.getValue().getName()));

        TableColumn<Item, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(i -> new javafx.beans.property.SimpleDoubleProperty(i.getValue().getPrice()).asObject());

        table.getColumns().addAll(idCol, nameCol, priceCol);

        // Form
        TextField idField = new TextField(); idField.setPromptText("ID");
        TextField nameField = new TextField(); nameField.setPromptText("Name");
        TextField priceField = new TextField(); priceField.setPromptText("Price");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button refreshBtn = new Button("Refresh");

        TextField searchField = new TextField(); searchField.setPromptText("Search...");
        ComboBox<String> searchType = new ComboBox<>(FXCollections.observableArrayList("name", "price"));
        searchType.setValue("name");
        ComboBox<String> orderBox = new ComboBox<>(FXCollections.observableArrayList("Ascending", "Descending"));
        orderBox.setValue("Ascending");

        HBox searchRow = new HBox(10, new Label("Search:"), searchField, searchType, orderBox);
        HBox formRow = new HBox(10, idField, nameField, priceField);
        HBox buttonRow = new HBox(10, addBtn, updateBtn, deleteBtn, refreshBtn);

        // Events
        addBtn.setOnAction(e -> {
            try {
                Item item = new Item(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        Double.parseDouble(priceField.getText())
                );
                controller.addItem(item);
                clear(idField, nameField, priceField);
                refresh();
            } catch (Exception ex) {
                show("Invalid input.");
            }
        });

        updateBtn.setOnAction(e -> {
            try {
                Item item = new Item(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        Double.parseDouble(priceField.getText())
                );
                controller.updateItem(item);
                refresh();
            } catch (Exception ex) {
                show("Update failed.");
            }
        });

        deleteBtn.setOnAction(e -> {
            try {
                controller.deleteItem(Integer.parseInt(idField.getText()));
                clear(idField, nameField, priceField);
                refresh();
            } catch (Exception ex) {
                show("Delete failed.");
            }
        });

        refreshBtn.setOnAction(e -> refresh());

        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                refresh(newVal, searchType.getValue(), orderBox.getValue()));
        searchType.setOnAction(e -> refresh(searchField.getText(), searchType.getValue(), orderBox.getValue()));
        orderBox.setOnAction(e -> refresh(searchField.getText(), searchType.getValue(), orderBox.getValue()));

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                idField.setText(String.valueOf(selected.getId()));
                nameField.setText(selected.getName());
                priceField.setText(String.valueOf(selected.getPrice()));
            }
        });

        layout.getChildren().addAll(title, searchRow, table, formRow, buttonRow);
        refresh();
        return layout;
    }

    private static void refresh() {
        data.clear();
        data.addAll(controller.search("", "name", true));
    }

    private static void refresh(String keyword, String field, String order) {
        data.clear();
        boolean asc = order.equalsIgnoreCase("Ascending");
        data.addAll(controller.search(keyword, field, asc));
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
