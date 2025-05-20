module org.example.further_programming {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.further_programming to javafx.fxml;
    exports org.example.further_programming;
}