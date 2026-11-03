package org.example.sudoku.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.sudoku.controllers.GameController;

import java.io.IOException;

public class GameView extends Stage {
    private GameController controller;

    public GameView() throws IOException {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/sudoku/game-view.fxml")
            );

            Parent root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root);

        setTitle("Sudoku Game");
        setScene(scene);
        sizeToScene();
    }

    public void startNewGame() {
        controller.generateInitialBoard();
    }

    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            GameViewHolder.INSTANCE = new GameView();
        }

        return GameViewHolder.INSTANCE;
    }

    private static class GameViewHolder {
        private static GameView INSTANCE = null;
    }
}