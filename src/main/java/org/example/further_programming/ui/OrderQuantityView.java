package org.example.further_programming.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.further_programming.controller.OrderItemController;
import org.example.further_programming.model.OrderItem;

public class OrderQuantityView {
    private static final OrderItemController controller = new OrderItemController();
    private static final ObservableList<OrderItem> data = FXCollections.observableArrayList();

    public static VBox create() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        Label title = new Label("📦 Order Quantities");

        TableView<OrderItem> table = new TableView<>(data);

        TableColumn<OrderItem, Integer> orderIdCol = new TableColumn<>("Order ID");
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        TableColumn<OrderItem, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<OrderItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table.getColumns().addAll(orderIdCol, itemNameCol, quantityCol);

        refresh();
        layout.getChildren().addAll(title, table);
        return layout;
    }

    private static void refresh() {
        data.clear();
        data.addAll(controller.getAll());
    }
}
