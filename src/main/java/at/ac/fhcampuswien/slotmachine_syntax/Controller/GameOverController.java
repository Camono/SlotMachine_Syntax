package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.SlotMachineApplication;
import javafx.application.Platform;
import at.ac.fhcampuswien.slotmachine_syntax.Controller.SlotMachineController;
import javafx.stage.Stage;

public class GameOverController {
    public void onNoButtonPressed() {
        Platform.exit();
    }
}