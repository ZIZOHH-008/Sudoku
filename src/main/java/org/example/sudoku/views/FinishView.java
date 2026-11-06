package org.example.sudoku.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;



/**
 * Represents the modal window displayed when the game is completed.
 */
public class FinishView extends Stage {

    /**
     * Creates and configures the finish screen.
     *
     * @param owner the game window that owns this dialog
     * @throws IOException if the FXML file cannot be loaded
     */
    public FinishView(Stage owner) throws IOException {
        // Load finish screen
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/sudoku/finish-view.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root);

        setTitle("Sudoku Game");
        setScene(scene);

        // Keep game window as owner
        initOwner(owner);

        // Make window modal
        initModality(Modality.WINDOW_MODAL);

        setResizable(false);
        sizeToScene();
    }
}