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

/**
 * Controls the welcome screen and handles game entry and exit actions.
 */
public class WelcomeController implements IGameLog {

    /** Provides confirmation and information dialogs. */
    private final IAlertBox alertBox = new AlertBox();

    /**
     * Confirms and opens a new Sudoku game.
     *
     * @param event the button click event
     * @throws IOException if an error occurs while loading the game view
     */
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

    /**
     * Closes the current window and clears the welcome view instance.
     *
     * @param event the button click event
     */
    @FXML
    public void exitGame(ActionEvent event) {
        logEvent("UI Event: Clicked Exit Game button");

        // Closes the current window from which the click was made
        Stage currentStage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        currentStage.close();

        // Clears the Singleton instance of the welcome view
        WelcomeView.deleteInstance();
    }
}