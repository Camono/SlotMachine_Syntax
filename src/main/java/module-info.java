module at.ac.fhcampuswien.slotmachine_syntax {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.ac.fhcampuswien.slotmachine_syntax to javafx.fxml;
    exports at.ac.fhcampuswien.slotmachine_syntax;
}