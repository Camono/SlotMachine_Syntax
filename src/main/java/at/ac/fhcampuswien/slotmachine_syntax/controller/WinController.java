package at.ac.fhcampuswien.slotmachine_syntax.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

public class WinController {
    @FXML
    private Label amountWonLabel;

    @FXML
    private Label youWonLabel;

    @FXML
    private Label creditsLabel;

    private Timeline animationTimeline;
    private Timeline blinkingTimeline;

    public void printAmount(double amount) {
        // IntelliJ hat AtomicInteger empfohlen wegen Lambda-Expression
        // ein AtomicInteger ermöglicht thread-sichere Operationen auf einer Ganzzahl ohne Datenkonflikte
        AtomicInteger start = new AtomicInteger();

        animationTimeline = new Timeline(
                new KeyFrame(Duration.millis(20), event -> {
                    start.getAndIncrement();
                    amountWonLabel.setText("" + start);

                    if (start.get() >= amount) {
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
