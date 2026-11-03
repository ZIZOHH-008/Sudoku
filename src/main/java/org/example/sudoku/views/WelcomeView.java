package org.example.sudoku.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Represents the welcome window of the Sudoku application.
 * <p>
 * Extends {@link Stage} to configure and display the welcome view.
 * Implements a Singleton pattern to provide a single reusable instance
 * of the window.
 * </p>
 */
public class WelcomeView extends Stage {

    /**
     * Creates and configures the welcome window.
     * <p>
     * Loads the welcome interface from its FXML file, creates the scene,
     * sets the window title, disables resizing, and adjusts the window
     * size to fit its content.
     *
     * @throws IOException if an error occurs while loading the FXML file
     */
    public WelcomeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/sudoku/welcome-view.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root);

        setTitle("Sudoku Game");
        setScene(scene);
        setResizable(false);
        sizeToScene();
    }

    /**
     * Returns the singleton instance of the welcome window.
     * <p>
     * If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of {@code WelcomeView}
     * @throws IOException if an error occurs while creating the window
     */
    public static WelcomeView getInstance() throws IOException {
        if (WelcomeViewHolder.INSTANCE == null) {
            WelcomeViewHolder.INSTANCE = new WelcomeView();
        }
        return WelcomeViewHolder.INSTANCE;
    }

    /**
     * Deletes the current singleton instance of the welcome window.
     * <p>
     * Sets the stored instance to {@code null}, allowing a new instance
     * to be created the next time {@link #getInstance()} is called.
     */
    public static void deleteInstance() {
        WelcomeViewHolder.INSTANCE = null;
    }

    /**
     * Holds the singleton instance of {@link WelcomeView}.
     * <p>
     * This private static nested class stores the shared instance
     * used by the Singleton implementation.
     */
    private static class WelcomeViewHolder {
        private static WelcomeView INSTANCE;
    }
}