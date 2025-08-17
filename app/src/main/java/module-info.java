module org.example{
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pathfindingjava to javafx.fxml;
    exports com.example.pathfindingjava;
}