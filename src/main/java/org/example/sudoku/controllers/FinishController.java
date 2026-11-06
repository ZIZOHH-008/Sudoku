package org.example.sudoku.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.sudoku.models.IGameLog;
import org.example.sudoku.views.WelcomeView;

import java.io.IOException;

public class FinishController implements IGameLog {

    @FXML
    private void onExitClick(ActionEvent event) {
        Stage finishStage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        // Get game window
        Stage gameStage = (Stage) finishStage.getOwner();

        finishStage.close();

        if (gameStage != null) {
            gameStage.hide();
        }

        try {
            // Back to main menu
            WelcomeView.getInstance().show();
        } catch (IOException e) {
            logEvent("Error returning to welcome view: " + e.getMessage());
        }
    }
}
