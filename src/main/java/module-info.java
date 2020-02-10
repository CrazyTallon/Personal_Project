module org.ta {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.ta to javafx.fxml;
    exports org.ta;
}