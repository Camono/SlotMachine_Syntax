module at.ac.fhcampuswien.slotmachine_syntax {
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    requires org.junit.jupiter.api;
    requires org.json;
    requires java.desktop;


    opens at.ac.fhcampuswien.slotmachine_syntax to javafx.fxml;
    exports at.ac.fhcampuswien.slotmachine_syntax;
    exports at.ac.fhcampuswien.slotmachine_syntax.Controller;
    opens at.ac.fhcampuswien.slotmachine_syntax.Controller to javafx.fxml;
}