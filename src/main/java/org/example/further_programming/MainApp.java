package org.example.further_programming;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.further_programming.database.DBInit;
import org.example.further_programming.ui.ItemView;
import org.example.further_programming.ui.Sidebar;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DBInit.init(); // Initialize DB tables and sample data

        BorderPane root = new BorderPane();
        root.setLeft(Sidebar.create(root));
        root.setCenter(ItemView.create()); // Default screen

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/org/example/further_programming/styles.css").toExternalForm());


        primaryStage.setTitle("🍽️ Restaurant Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
