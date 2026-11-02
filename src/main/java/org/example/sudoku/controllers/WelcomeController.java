package org.example.sudoku.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.sudoku.models.AlertBox;
import org.example.sudoku.models.IAlertBox;
import org.example.sudoku.models.IGameLog;
import org.example.sudoku.views.GameView;
import org.example.sudoku.views.WelcomeView;

import java.io.IOException;

public class WelcomeController implements IGameLog {

    private final IAlertBox alertBox = new AlertBox();

    @FXML
    public void enterGame(ActionEvent event) throws IOException {
        logEvent("UI Event: Clicked Start Game button");

        boolean response = alertBox.showConfirmBox(
                "Start Game",
                "¡Hi there!",
                "¿Do you want to start a game?"
        );

        if (response) {
            logEvent("UI Event: Load Game View");

            Stage welcomeStage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            GameView gameView = GameView.getInstance();
            gameView.startNewGame();
            gameView.show();

            welcomeStage.hide();

        } else {
            logEvent("UI Event: Cancel game");
        }
    }

    @FXML
    private void exitGame() {
        logEvent("UI Event: Clicked Exit Game button");

        try {
            WelcomeView.getInstance().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}