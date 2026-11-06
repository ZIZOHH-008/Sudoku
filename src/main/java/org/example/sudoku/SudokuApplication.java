package org.example.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



/**
 * Main application class for the Sudoku game.
 * <p>
 * Initializes the JavaFX application, loads the welcome view,
 * configures the primary stage, and displays the application window.
 * </p>
 */
public class SudokuApplication extends Application {

    /**
     * Initializes and displays the main application window.
     * <p>
     * Loads the welcome view from its FXML file, creates a scene,
     * configures the stage title and scene, adjusts the window size
     * to fit its content, and displays it.
     *
     * @param stage the primary stage used to display the application
     * @throws IOException if an error occurs while loading the FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SudokuApplication.class.getResource("welcome-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Sudoku Game");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

}
