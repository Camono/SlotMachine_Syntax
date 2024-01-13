package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
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

import java.util.Collections;
import java.util.List;

public class SlotMachineController {

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private ImageView symbol1ImageView;

    @FXML
    private ImageView symbol2ImageView;

    @FXML
    private ImageView symbol3ImageView;

    @FXML
    private ImageView symbol4ImageView;

    @FXML
    private ImageView symbol5ImageView;

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
    private GameManager gameManager = new GameManager(1000);
    @FXML
    private void onSpinButtonClick() {
        // Add your code here
        List<Symbol> spinResults = gameManager.createSpinResult();
        GameResult gameResult = gameManager.calculateWinnings(spinResults);
        setSymbolImages(gameResult.getSymbols());
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
        setSymbolImages(Collections.emptyList());
        betLabel.setText(gameManager.getBet()+"");
    }

    private void setSymbolImages(List<Symbol> symbols) {
        if (symbols != null && !symbols.isEmpty()) {
            //on game result fill
            symbol1ImageView.setImage(new Image(symbols.get(0).getImagePath()));
            symbol2ImageView.setImage(new Image(symbols.get(1).getImagePath()));
            symbol3ImageView.setImage(new Image(symbols.get(2).getImagePath()));
            symbol4ImageView.setImage(new Image(symbols.get(3).getImagePath()));
            symbol5ImageView.setImage(new Image(symbols.get(4).getImagePath()));
        } else {
            //initial fill
            symbol1ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.U1).getImagePath()));
            symbol2ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.U6).getImagePath()));
            symbol3ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.RED_BULL).getImagePath()));
            symbol4ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.MARLBORO).getImagePath()));
            symbol5ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.LUGNER).getImagePath()));
        }
    }
}

