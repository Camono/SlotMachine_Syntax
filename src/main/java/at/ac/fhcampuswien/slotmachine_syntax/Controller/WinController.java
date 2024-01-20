package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class WinController {
    @FXML
    private Label amountWonLabel;

    @FXML
    private Label youWonLabel;

    @FXML
    private Label creditsLabel;

    private int start;
    private Timeline animationTimeline;
    private Timeline blinkingTimeline;

    public void printAmount(double amount) {
        start = 0;

        animationTimeline = new Timeline(
                new KeyFrame(Duration.millis(20), event -> {
                    start++;
                    amountWonLabel.setText("" + start);

                    if (start >= amount) {
                        animationTimeline.stop();
                        blinkingTimeline.play();
                        youWonLabel.setVisible(true);
                        creditsLabel.setVisible(true);
                        amountWonLabel.setText(amount + "");
                    }
                })
        );
        blinkingTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.75), event -> {
                    if(amountWonLabel.getOpacity() == 0.5) {
                        amountWonLabel.setOpacity(1);
                    } else {
                        amountWonLabel.setOpacity(0.5);
                    }
                })
        );
        blinkingTimeline.setCycleCount(25000);
        animationTimeline.setCycleCount(25000);
        animationTimeline.play();
    }
}
