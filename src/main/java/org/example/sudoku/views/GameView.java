package org.example.sudoku.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.sudoku.controllers.GameController;

import java.io.IOException;
/**
 * Represents the main game window of the Sudoku application.
 * <p>
 * Extends {@link Stage} to display the game interface and manages
 * the interaction with its associated {@link GameController}.
 * </p>
 */
public class GameView extends Stage {

    /**
     * Controller responsible for managing the game interface and its events.
     */
    private GameController controller;

    /**
     * Creates and configures the game window.
     * <p>
     * Loads the game interface from its FXML file, retrieves the associated
     * controller, creates the scene, sets the window title, and adjusts
     * the window size to fit its content.
     *
     * @throws IOException if an error occurs while loading the FXML file
     */
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

    /**
     * Starts a new Sudoku game by requesting the controller
     * to generate the initial game board.
     */
    public void startNewGame() {
        controller.generateInitialBoard();
    }

    /**
     * Returns the singleton instance of the game window.
     * <p>
     * If no instance exists, a new {@code GameView} is created
     * and stored for subsequent calls.
     *
     * @return the singleton instance of {@code GameView}
     * @throws IOException if an error occurs while creating the window
     */
    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            GameViewHolder.INSTANCE = new GameView();
        }

        return GameViewHolder.INSTANCE;
    }

    /**
     * Holds the singleton instance of {@link GameView}.
     * <p>
     * This private static nested class stores the shared instance
     * used by the Singleton implementation.
     */
    private static class GameViewHolder {
        private static GameView INSTANCE = null;
    }
}