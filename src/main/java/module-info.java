module com.example.librarysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.librarysystem to javafx.fxml;
    opens com.example.librarysystem.controllers to javafx.fxml;
    opens com.example.librarysystem.classes to javafx.base;
    opens com.example.librarysystem.utils to javafx.base;
    exports com.example.librarysystem;
}