package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
import at.ac.fhcampuswien.slotmachine_syntax.SlotMachineApplication;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SlotMachineController {

    @FXML
    private ImageView soundSymbolImageView;
    @FXML
    private ImageView mortgageSymbolImageView;
    @FXML
    private ImageView musicSymbolImageView;
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

    private final GameManager gameManager = new GameManager(1000);

    private Timeline cooldownTimeline;
    private boolean soundEnabled = true;

    // MediaPlayer darf nicht lokal sein (Probleme mit Sound)
    private MediaPlayer spinSound;
    private MediaPlayer soundEffect;
    private MediaPlayer backgroundMusic;

    @FXML
    private void onSpinButtonClick() {
        playSpinSound("src/main/resources/sounds/SpinSound.mp3");
        double newBalance = gameManager.getBalance()-gameManager.getBet();
        if (newBalance < 0) {
            newBalance = 0;
        }
        balanceLabel.setText(newBalance + "");
        decreaseBetBtn.setDisable(true);
        increaseBetBtn.setDisable(true);

        List<Symbol> spinResults = gameManager.createSpinResult();
        GameResult gameResult = gameManager.calculateWinnings(spinResults);
        handleSpinButtonCountdown();
        animateSymbolsBeforeRevealingResults(gameResult);
    }

    private void animateSymbolsBeforeRevealingResults(GameResult gameResult) {
        Timeline animationTimeline = new Timeline(new KeyFrame(
                Duration.millis(250),
                event -> {
                    symbol1ImageView.setImage(new Image(gameManager.pickRandomSymbol(Collections.emptyList()).getImagePath()));
                    centerImage(symbol1ImageView);
                    symbol2ImageView.setImage(new Image(gameManager.pickRandomSymbol(Collections.emptyList()).getImagePath()));
                    centerImage(symbol2ImageView);
                    symbol3ImageView.setImage(new Image(gameManager.pickRandomSymbol(Collections.emptyList()).getImagePath()));
                    centerImage(symbol3ImageView);
                    symbol4ImageView.setImage(new Image(gameManager.pickRandomSymbol(Collections.emptyList()).getImagePath()));
                    centerImage(symbol4ImageView);
                    symbol5ImageView.setImage(new Image(gameManager.pickRandomSymbol(Collections.emptyList()).getImagePath()));
                    centerImage(symbol5ImageView);
                }
        ));

        animationTimeline.setCycleCount(10);

        animationTimeline.setOnFinished(e -> {
            setSymbolImages(gameResult.getSymbols());

            // Big Win Popup
            if ((gameResult.getProfit()/gameManager.getBet()) >= 2.5) {
                try {
                    openWinPopup(gameResult.getProfit());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                profitLabel.setText("+" + gameResult.getProfit());
                balanceLabel.setText(gameResult.getNewBalance() + "");
            // Game Over Popup
            } else if (gameResult.getNewBalance() <= 950) {
                try {
                    openGameOverPopup();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                balanceLabel.setText("0");
            // Wenn kein Big Win oder Game Over
            } else {
                // Wenn nichts gewonnen wurde
                if (gameResult.getProfit() == 0) {
                    playSound("src/main/resources/sounds/Lost.mp3");
                    profitLabel.setText("-" + gameManager.getBet());
                // Wenn weniger als Einsatz gewonnen wurde
                } else if ((gameResult.getProfit() - gameManager.getBet()) < 0) {
                    profitLabel.setText("" + (gameResult.getProfit() - gameManager.getBet()));
                // Wenn mehr als Einsatz gewonnen wurde
                } else if ((gameResult.getProfit() - gameManager.getBet()) > 0) {
                    playSound("src/main/resources/sounds/Nice.mp3");
                    profitLabel.setText("+" + gameResult.getProfit());
                }
                balanceLabel.setText(gameResult.getNewBalance() + "");
            }
            // Bet kann erst nach Spin Abfolge geändert werden
            decreaseBetBtn.setDisable(false);
            increaseBetBtn.setDisable(false);
            updateGlowEffect();
        });

        animationTimeline.play();
    }

    private void updateGlowEffect() {
        double gameCredit = gameManager.getBalance();
        DropShadow glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setRadius(20);
        glow.setSpread(0.9);

        // Animation für den Puls Effekt beim Hypothek Button
        Timeline pulsate = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.radiusProperty(), 10, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(glow.radiusProperty(), 20, Interpolator.EASE_IN))
        );
        // Animation wird unendlich lange wiederholt
        pulsate.setCycleCount(Animation.INDEFINITE);
        pulsate.setAutoReverse(true);

        // Erst ab <100 Credits wird Effekt angezeigt
        if (gameCredit < 100) {
            mortgageSymbolImageView.setEffect(glow);
            pulsate.play();
        } else {
            mortgageSymbolImageView.setEffect(null);
            pulsate.stop();
        }
    }

    private void handleSpinButtonCountdown() {
        // IntelliJ hat AtomicInteger empfohlen wegen Lambda-Expression
        // ein AtomicInteger ermöglicht thread-sichere Operationen auf einer Ganzzahl ohne Datenkonflikte
        AtomicInteger remainingCooldown = new AtomicInteger();
        spinLabel.setText("3");
        spinBtn.setDisable(true);
        spinBtn.setStyle("-fx-background-color: #232b2d;");

        // in seconds
        int cooldownDuration = 3;
        remainingCooldown.set(cooldownDuration);

        // Spin Button wird disabled und Text wird überschrieben, von 3 bis 0, Spieler Schutzmaßnahme
        cooldownTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    remainingCooldown.getAndDecrement();
                    spinLabel.setText("" + remainingCooldown);
                    if (remainingCooldown.get() == 0) {
                        cooldownTimeline.stop();
                        spinBtn.setDisable(false);
                        spinLabel.setText("SPIN");
                        spinBtn.setOpacity(0.0);
                        spinBtn.setStyle("-fx-background-color: transparent;");
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

    // Öffnet Info Popup bei Info Button Click
    @FXML
    private void onInfoButtonClick() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SlotMachineApplication.class.getResource("info.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Info");
            stage.setScene(new Scene(fxmlLoader.load(), 896, 512));
            stage.show();
        } catch (IOException e) {
            throw new IOException("Failed to load Information panel: " + e.getMessage());
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

    public void onSpinButtonPressed() {
        spinBtn.setOpacity(0.15);
    }

    public void onSpinButtonReleased() {
        spinBtn.setOpacity(0.6);
    }

    public void onIncreaseBetButtonPressed() {
        increaseBetBtn.setOpacity(0.3);
    }

    public void onIncreaseBetButtonReleased() {
        increaseBetBtn.setOpacity(0.0);
    }

    public void onDecreaseBetButtonPressed() {
        decreaseBetBtn.setOpacity(0.3);
    }

    public void onDecreaseBetButtonReleased() {
        decreaseBetBtn.setOpacity(0.0);
    }

    public void onInfoButtonPressed() {
        infoBtn.setOpacity(0.3);
    }

    public void onInfoButtonReleased() {
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
        backgroundMusic.setOnEndOfMedia(() -> {
            backgroundMusic.seek(Duration.ZERO);
            backgroundMusic.play();
        });
        backgroundMusic.setAutoPlay(true);
    }

    public void onSoundButtonClick() {
        soundEnabled = !soundEnabled; // Toggle sound on/off
        if (soundEnabled) {
            Image soundOnImage = new Image("image/Audio.png");
            soundSymbolImageView.setImage(soundOnImage);
        } else {
            Image soundOffImage = new Image("image/AudioMute.png");
            soundSymbolImageView.setImage(soundOffImage);
        }
    }

    public void openWinPopup(double amount) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SlotMachineApplication.class.getResource("popup/win.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Congratulations!");
            stage.setScene(new Scene(fxmlLoader.load(), 610, 512));
            stage.setResizable(false);
            stage.show();

            WinController winCon = fxmlLoader.getController();
            winCon.printAmount(amount);
        } catch (IOException e) {
            throw new IOException("Failed to load Win Popup panel: " + e.getMessage());
        }
    }

    public void openGameOverPopup() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SlotMachineApplication.class.getResource("popup/gameOver.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Game Over!");
            stage.setScene(new Scene(fxmlLoader.load(), 550, 510));
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event -> Platform.exit());
        } catch (IOException e) {
            throw new IOException("Failed to load Game Over Popup panel: " + e.getMessage());
        }
    }

    public void onMortgageButtonClick() throws IOException, URISyntaxException {
        String url = "https://www.raiffeisen.at/de/privatkunden/kredit-leasing/wissenswertes-zum-thema-finanzieren/infos-zur-hypothek.html";
        try {
            Desktop.getDesktop().browse(new URI(url));
            // Handle exceptions, e.g., if the URL is invalid or if there is no default browser.
        } catch (IOException e) {
            throw new IOException("Failed to load URL: " + e.getMessage());
        } catch (URISyntaxException u) {
            throw new URISyntaxException("Malformed URL: ", u.getMessage());
        }
    }

    public void onMisteryButtonClick() throws IOException, URISyntaxException {
        String url = "https://www.yout-ube.com/watch?v=dQw4w9WgXcQ";
        try {
            Desktop.getDesktop().browse(new URI(url));
            // Handle exceptions, e.g., if the URL is invalid or if there is no default browser.
        } catch (IOException e) {
            throw new IOException("Failed to load URL: " + e.getMessage());
        } catch (URISyntaxException u) {
            throw new URISyntaxException("Malformed URL: ", u.getMessage());
        }
    }

    public void onMusicButtonClick() {
        if (backgroundMusic.getVolume() != 0) {
            backgroundMusic.setVolume(0);
            Image musicOff = new Image("image/MusicMute.png");
            musicSymbolImageView.setImage(musicOff);
        } else {
            backgroundMusic.setVolume(1);
            Image musicOn = new Image("image/Music.png");
            musicSymbolImageView.setImage(musicOn);
        }
    }
}