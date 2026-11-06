package org.example.sudoku.models;

/**
 * Defines methods for displaying alert dialogs.
 */
public interface IAlertBox {

    /**
     * Displays a confirmation dialog.
     *
     * @param title the dialog title
     * @param header the dialog header
     * @param message the dialog message
     * @return {@code true} if the user confirms; {@code false} otherwise
     */
    boolean showConfirmBox(String title, String header, String message);

    /**
     * Displays an information dialog.
     *
     * @param title the dialog title
     * @param header the dialog header
     * @param message the dialog message
     */
    void showInformationBox(String title, String header, String message);
}