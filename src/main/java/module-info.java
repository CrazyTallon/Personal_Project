module org.ta {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.ta to javafx.fxml;
    exports org.ta;
}