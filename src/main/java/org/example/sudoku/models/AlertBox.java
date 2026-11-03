package org.example.sudoku.models;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Implements confirmation and information alert dialogs.
 */
public class AlertBox implements IAlertBox {

    /**
     * Displays a confirmation dialog and checks the user's response.
     *
     * @param title the dialog title
     * @param header the dialog header
     * @param message the dialog message
     * @return {@code true} if the user selects OK; {@code false} otherwise
     */
    @Override
    public boolean showConfirmBox(String title, String header, String message) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        Optional<ButtonType> response = alert.showAndWait();

        return response.isPresent() && response.get() == ButtonType.OK;
    }

    /**
     * Displays an information dialog.
     *
     * @param title the dialog title
     * @param header the dialog header
     * @param message the dialog message
     */
    @Override
    public void showInformationBox(String title, String header, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }
}