package at.ac.fhcampuswien.slotmachine_syntax;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SlotMachineApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SlotMachineApplication.class.getResource("slotMachine.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1180, 675);
        stage.setScene(scene);
        stage.setTitle("Slot Machine");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}