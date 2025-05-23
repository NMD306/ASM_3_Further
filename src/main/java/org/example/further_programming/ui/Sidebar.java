package org.example.further_programming.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Sidebar {
    public static VBox create(BorderPane root) {
        VBox menu = new VBox(15);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #2c3e50;");

        Button itemsBtn = new Button("📦 Items");
        Button customersBtn = new Button("👤 Customers");
        Button ordersBtn = new Button("🧾 Orders");
        Button deliverymenBtn = new Button("🚚 Deliverymen");
        Button orderQuantitiesBtn = new Button("📊 Order Quantities");  // New button

        for (Button btn : new Button[]{itemsBtn, customersBtn, ordersBtn, deliverymenBtn, orderQuantitiesBtn}) {
            btn.setPrefWidth(160);
            btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        }

        itemsBtn.setOnAction(e -> root.setCenter(ItemView.create()));
        customersBtn.setOnAction(e -> root.setCenter(CustomerView.create()));
        ordersBtn.setOnAction(e -> root.setCenter(OrderView.create()));
        deliverymenBtn.setOnAction(e -> root.setCenter(DeliverymanView.create()));
        orderQuantitiesBtn.setOnAction(e -> root.setCenter(OrderQuantityView.create()));  // Hook up view

        menu.getChildren().addAll(itemsBtn, customersBtn, ordersBtn, deliverymenBtn, orderQuantitiesBtn);
        return menu;
    }
}
