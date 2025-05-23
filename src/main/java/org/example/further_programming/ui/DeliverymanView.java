package org.example.further_programming.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.further_programming.controller.DeliverymanController;
import org.example.further_programming.model.Deliveryman;

public class DeliverymanView {
    private static final DeliverymanController controller = new DeliverymanController();
    private static final ObservableList<Deliveryman> data = FXCollections.observableArrayList();

    public static VBox create() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label title = new Label("🚚 Deliveryman Management");

        TableView<Deliveryman> table = new TableView<>(data);
        table.setPrefHeight(300);

        TableColumn<Deliveryman, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getId()).asObject());

        TableColumn<Deliveryman, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));

        TableColumn<Deliveryman, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPhone()));

        table.getColumns().addAll(idCol, nameCol, phoneCol);

        TextField idField = new TextField(); idField.setPromptText("ID");
        TextField nameField = new TextField(); nameField.setPromptText("Name");
        TextField phoneField = new TextField(); phoneField.setPromptText("Phone");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button refreshBtn = new Button("Refresh");

        TextField searchField = new TextField(); searchField.setPromptText("Search...");
        ComboBox<String> searchType = new ComboBox<>(FXCollections.observableArrayList("name", "phone"));
        searchType.setValue("name");

        ComboBox<String> orderBox = new ComboBox<>(FXCollections.observableArrayList("Ascending", "Descending"));
        orderBox.setValue("Ascending");

        HBox searchRow = new HBox(10, new Label("Search:"), searchField, searchType, orderBox);
        HBox formRow = new HBox(10, idField, nameField, phoneField);
        HBox buttonRow = new HBox(10, addBtn, updateBtn, deleteBtn, refreshBtn);

        addBtn.setOnAction(e -> {
            try {
                Deliveryman d = new Deliveryman(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        phoneField.getText()
                );
                controller.addDeliveryman(d);
                clear(idField, nameField, phoneField);
                refresh();
            } catch (Exception ex) {
                show("Invalid input.");
            }
        });

        updateBtn.setOnAction(e -> {
            try {
                Deliveryman d = new Deliveryman(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        phoneField.getText()
                );
                controller.updateDeliveryman(d);
                refresh();
            } catch (Exception ex) {
                show("Update failed.");
            }
        });

        deleteBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                controller.deleteDeliveryman(id);
                clear(idField, nameField, phoneField);
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
                phoneField.setText(selected.getPhone());
            }
        });

        layout.getChildren().addAll(title, searchRow, table, formRow, buttonRow);
        refresh();
        return layout;
    }

    private static void refresh() {
        refresh("", "name", "Ascending");
    }

    private static void refresh(String keyword, String field, String order) {
        boolean asc = order.equalsIgnoreCase("Ascending");
        data.clear();
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
