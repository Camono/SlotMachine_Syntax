package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import javafx.application.Platform;

public class GameOverController {
    public void onNoButtonPressed() {
        Platform.exit();
    }
}