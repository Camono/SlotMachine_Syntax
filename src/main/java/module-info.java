module at.ac.fhcampuswien.slotmachine_syntax {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;


    opens at.ac.fhcampuswien.slotmachine_syntax to javafx.fxml;
    exports at.ac.fhcampuswien.slotmachine_syntax;
}