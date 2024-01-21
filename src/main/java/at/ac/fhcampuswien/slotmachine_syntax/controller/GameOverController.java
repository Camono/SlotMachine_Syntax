package at.ac.fhcampuswien.slotmachine_syntax.controller;

import javafx.application.Platform;

public class GameOverController {
    public void onNoButtonPressed() {
        Platform.exit();
    }
}