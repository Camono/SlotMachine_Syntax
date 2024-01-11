package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
import at.ac.fhcampuswien.slotmachine_syntax.Model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlotMachineController {

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private ImageView symbol1;

    @FXML
    private ImageView symbol2;

    @FXML
    private ImageView symbol3;

    @FXML
    private ImageView symbol4;

    @FXML
    private ImageView symbol5;

    @FXML
    private Button spinBtn;

    @FXML
    private Button increaseBetBtn;

    @FXML
    private Button decreaseBetBtn;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label betLabel;

    @FXML
    private Button infoBtn;

    // Event handler for spinBtn
    private final GameManager gameManager = new GameManager(1000);
    private Map<String, Image> imageCache = new HashMap<>();


    @FXML
    private void onSpinButtonClick() {
        // Add your code here
        List<Symbol> spinResults = gameManager.createSpinResult();
        GameResult gameResult = gameManager.calculateWinnings(spinResults);
        updateSymbolImage(symbol1,gameResult.getSymbols().get(0).getImagePath());
        updateSymbolImage(symbol2,gameResult.getSymbols().get(1).getImagePath());
        updateSymbolImage(symbol3,gameResult.getSymbols().get(2).getImagePath());
        updateSymbolImage(symbol4,gameResult.getSymbols().get(3).getImagePath());
        updateSymbolImage(symbol5,gameResult.getSymbols().get(4).getImagePath());
        balanceLabel.setText(gameResult.getNewBalance() + "");
    }

    private void showAlertWithAutoClose(double profit) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Gewinn: " + profit);


        // Set up the PauseTransition
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> alert.close());

        alert.show();
        delay.play(); // Start the delay
    }

    // Event handler for increaseBetBtn
    @FXML
    private void onIncreaseBetButtonClick() {
        betLabel.setText(gameManager.increaseBet() + "");
    }

    // Event handler for decreaseBetBtn
    @FXML
    private void onDecreaseBetButtonClick() {
        betLabel.setText(gameManager.decreaseBet() + "");
    }

    // Event handler for infoBtn
    private void showSymbolPopup() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Symbols Information");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Creating placeholders for 8 symbols and their descriptions
        for (int i = 0; i < 8; i++) {
            Label symbolLabel = new Label("Symbol " + (i + 1));
            Label descriptionLabel = new Label("Beschreibung " + (i + 1));
            grid.add(symbolLabel, 0, i); // Column 0, Row i
            grid.add(descriptionLabel, 1, i); // Column 1, Row i
        }

        alert.getDialogPane().setContent(grid);

        alert.showAndWait();
    }
    @FXML
    private void onInfoButtonClick() {

    }

    // Initialize method if needed
    @FXML
    public void initialize() {
        updateSymbolImage(symbol1,gameManager.getGameDataJsonLoader().getImageByName(SymbolType.U1));
        updateSymbolImage(symbol2,gameManager.getGameDataJsonLoader().getImageByName(SymbolType.U1));
        updateSymbolImage(symbol3,gameManager.getGameDataJsonLoader().getImageByName(SymbolType.U1));
        updateSymbolImage(symbol4,gameManager.getGameDataJsonLoader().getImageByName(SymbolType.U1));
        updateSymbolImage(symbol5,gameManager.getGameDataJsonLoader().getImageByName(SymbolType.U1));
    }

    private void updateSymbolImage(ImageView imageView, String imagePath) {
        Image image = imageCache.get(imagePath);
        if (image == null) {
            image = new Image(imagePath);
            imageCache.put(imagePath, image);
        }
        imageView.setImage(image);
    }
}

