package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
import at.ac.fhcampuswien.slotmachine_syntax.SlotMachineApplication;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SlotMachineController {

    public ImageView soundSymbolImageView;
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
    private Label spinLabel;

    @FXML
    private Label profitLabel;

    @FXML
    private Button infoBtn;

    public GameManager gameManager = new GameManager(1000);
    private int remainingCooldown;
    private Timeline cooldownTimeline;
    private boolean soundEnabled = true;
    MediaPlayer spinSound;
    MediaPlayer soundEffect;
    MediaPlayer backgroundMusic;

    @FXML
    private void onSpinButtonClick() {
        playSpinSound("src/main/resources/sounds/SpinSound.mp3");
        handleSpinButtonCountdown();

        // Trigger spin animations
        playSpinAnimation(symbol1ImageView, 2);
        playSpinAnimation(symbol2ImageView, 2);
        playSpinAnimation(symbol3ImageView, 2);
        playSpinAnimation(symbol4ImageView, 2);
        playSpinAnimation(symbol5ImageView, 2);
    }

    private void playSpinAnimation(ImageView imageView, int durationInSeconds) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(durationInSeconds), imageView);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setCycleCount(2);
        rotateTransition.setAutoReverse(false);
        rotateTransition.play();
    }
    private void handleSpinButtonCountdown() {
        spinLabel.setText("3");
        spinBtn.setDisable(true);
        spinBtn.setStyle("-fx-background-color: #232b2d;");

        // in seconds
        int cooldownDuration = 3;
        remainingCooldown = cooldownDuration;

        cooldownTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    remainingCooldown--;
                    spinLabel.setText("" + remainingCooldown);

                    if (remainingCooldown == 0) {
                        cooldownTimeline.stop();
                        spinBtn.setDisable(false);
                        spinLabel.setText("SPIN");
                        spinBtn.setOpacity(0.0);
                        spinBtn.setStyle("-fx-background-color: transparent;");

                        List<Symbol> spinResults = gameManager.createSpinResult();
                        GameResult gameResult = gameManager.calculateWinnings(spinResults);
                        setSymbolImages(gameResult.getSymbols());
                        if (gameResult.getProfit() >= 100) {
                            openWinPopup(gameResult.getProfit());
                            balanceLabel.setText(gameResult.getNewBalance() + "");
                        } else if (gameResult.getNewBalance() <= 0) {
                            openGameOverPopup();
                            balanceLabel.setText("0");
                        } else {
                            if (gameResult.getProfit() >= 15) {
                                playSound("src/main/resources/sounds/Nice.mp3");
                            } else if (gameResult.getProfit() == 0){
                                playSound("src/main/resources/sounds/Lost.mp3");
                            }
                            balanceLabel.setText(gameResult.getNewBalance() + "");
                        } if (gameResult.getProfit() > 0) {
                            profitLabel.setText("+" + gameResult.getProfit());
                        } else {
                            profitLabel.setText("+0");
                        }
                    }
                })
        );
        cooldownTimeline.setCycleCount(cooldownDuration);
        cooldownTimeline.play();
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

    @FXML
    private void onInfoButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SlotMachineApplication.class.getResource("info.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Info");
            stage.setScene(new Scene(fxmlLoader.load(), 896, 512));
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load Information panel.");
        }
    }

    // Initialize method if needed
    @FXML
    public void initialize() {
        setSymbolImages(Collections.emptyList());
        centerImage(soundSymbolImageView);
        betLabel.setText(gameManager.getBet() + "");
        spinLabel.setText("SPIN");
        playBackground("src/main/resources/sounds/BackgroundMusic.mp3");
    }

    private void setSymbolImages(List<Symbol> symbols) {
        if (symbols != null && !symbols.isEmpty()) {
            //on game result fill
            symbol1ImageView.setImage(new Image(symbols.get(0).getImagePath()));
            centerImage(symbol1ImageView);
            symbol2ImageView.setImage(new Image(symbols.get(1).getImagePath()));
            centerImage(symbol2ImageView);
            symbol3ImageView.setImage(new Image(symbols.get(2).getImagePath()));
            centerImage(symbol3ImageView);
            symbol4ImageView.setImage(new Image(symbols.get(3).getImagePath()));
            centerImage(symbol4ImageView);
            symbol5ImageView.setImage(new Image(symbols.get(4).getImagePath()));
            centerImage(symbol5ImageView);
        } else {
            //initial fill
            symbol1ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.U1).getImagePath()));
            centerImage(symbol1ImageView);
            symbol2ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.U6).getImagePath()));
            centerImage(symbol2ImageView);
            symbol3ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.RED_BULL).getImagePath()));
            centerImage(symbol3ImageView);
            symbol4ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.MARLBORO).getImagePath()));
            centerImage(symbol4ImageView);
            symbol5ImageView.setImage(new Image(gameManager.getSymbol(SymbolType.LUGNER).getImagePath()));
            centerImage(symbol5ImageView);
        }
    }

    private void centerImage(ImageView iv) {
        double ratioX = iv.getFitWidth() / iv.getImage().getWidth();
        double ratioY = iv.getFitHeight() / iv.getImage().getHeight();

        double temp = Math.min(ratioX, ratioY);

        double width = iv.getImage().getWidth() * temp;
        double height = iv.getImage().getHeight() * temp;

        iv.setX((iv.getFitWidth() - width) / 2);
        iv.setY((iv.getFitHeight() - height) / 2);
    }

    public void onSpinButtonPressed(MouseEvent mouseEvent) {
        spinBtn.setOpacity(0.15);
    }

    public void onSpinButtonReleased(MouseEvent mouseEvent) {
        spinBtn.setOpacity(0.6);
    }

    public void onIncreaseBetButtonPressed(MouseEvent mouseEvent) {
        increaseBetBtn.setOpacity(0.3);
    }

    public void onIncreaseBetButtonReleased(MouseEvent mouseEvent) {
        increaseBetBtn.setOpacity(0.0);
    }

    public void onDecreaseBetButtonPressed(MouseEvent mouseEvent) {
        decreaseBetBtn.setOpacity(0.3);
    }

    public void onDecreaseBetButtonReleased(MouseEvent mouseEvent) {
        decreaseBetBtn.setOpacity(0.0);
    }

    public void onInfoButtonPressed(MouseEvent mouseEvent) {
        infoBtn.setOpacity(0.3);
    }

    public void onInfoButtonReleased(MouseEvent mouseEvent) {
        infoBtn.setOpacity(0.0);
    }

    public void playSound(String soundFilePath) {
        if (soundEnabled) {
            Media media = new Media(new File(soundFilePath).toURI().toString());
            soundEffect = new MediaPlayer(media);
            soundEffect.play();
        }
    }

    public void playSpinSound(String soundFilePath) {
        if (soundEnabled) {
            Media media = new Media(new File(soundFilePath).toURI().toString());
            spinSound = new MediaPlayer(media);
            spinSound.play();
        }
    }

    public void playBackground(String soundFilePath) {
        Media media = new Media(new File(soundFilePath).toURI().toString());
        backgroundMusic = new MediaPlayer(media);
        backgroundMusic.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                backgroundMusic.seek(Duration.ZERO);
                backgroundMusic.play();
            }
        });
        backgroundMusic.setAutoPlay(true);
    }

    public void onSoundButtonClick(ActionEvent actionEvent) {
        soundEnabled = !soundEnabled; // Toggle sound on/off
        if (soundEnabled) {
            Image soundOnImage = new Image("image/Audio.png");
            soundSymbolImageView.setImage(soundOnImage);
        } else {
            Image soundOffImage = new Image("image/AudioMute.png");
            soundSymbolImageView.setImage(soundOffImage);
        }
    }

    public void openWinPopup(double amount) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SlotMachineApplication.class.getResource("popup/win.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Congratulations!");
            stage.setScene(new Scene(fxmlLoader.load(), 896, 512));
            stage.show();
            spinBtn.setDisable(true);
            spinBtn.setOpacity(0.5);
            spinBtn.setStyle("-fx-background-color: #232b2d;");

            WinController winCon = fxmlLoader.getController();
            winCon.printAmount(amount);

            stage.setOnCloseRequest(event -> {
                spinBtn.setDisable(false);
                spinBtn.setOpacity(0.0);
                spinBtn.setStyle("-fx-background-color: transparent;");
            });
        } catch (IOException e) {
            System.out.println("Failed to load Win-Popup.");
        }
    }

    public void openGameOverPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SlotMachineApplication.class.getResource("popup/gameOver.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Game Over!");
            stage.setScene(new Scene(fxmlLoader.load(), 715, 512));
            stage.show();
            spinBtn.setDisable(true);
            spinBtn.setOpacity(0.5);
            spinBtn.setStyle("-fx-background-color: #232b2d;");

            stage.setOnCloseRequest(event -> {
                Platform.exit();
            });
        } catch (IOException e) {
            System.out.println("Failed to load Win-Popup.");
        }
    }
}