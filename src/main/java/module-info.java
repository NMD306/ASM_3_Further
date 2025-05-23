module org.example.further_programming {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.further_programming.model to javafx.base, javafx.fxml;

    exports org.example.further_programming;
}