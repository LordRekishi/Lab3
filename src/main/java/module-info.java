module se.iths.java21.patrik.lab3.lab3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens se.iths.java21.patrik.lab3 to javafx.fxml;
    exports se.iths.java21.patrik.lab3;
    exports se.iths.java21.patrik.lab3.shapes;
    opens se.iths.java21.patrik.lab3.shapes to javafx.fxml;
    exports se.iths.java21.patrik.lab3.tools;
    opens se.iths.java21.patrik.lab3.tools to javafx.fxml;
}