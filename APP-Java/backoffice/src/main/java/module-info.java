module com.ford {
    requires transitive javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;

    opens com.ford to javafx.fxml;
    exports com.ford;
}
