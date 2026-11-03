package org.example.sudoku.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.sudoku.models.*;
import org.example.sudoku.views.WelcomeView;

import java.io.IOException;
import java.util.Optional;

/**
 * Main Controller for the 6x6 Sudoku Game.
 * Manages grid creation, button interactions, real-time validation (HU-4), dynamic UI messaging, and hint logic (HU-5).
 */
public class GameController implements IGameLog {

    @FXML
    private GridPane sudokuGrid;

    @FXML
    private Label errorMessageLabel;

    private final SudokuGame game = new SudokuGame();
    private final IAlertBox alertBox = new AlertBox();
    private final TextField[][] cells = new TextField[6][6];

    @FXML
    public void initialize() {
        // Clear default layout constraints to prevent grid distortion
        sudokuGrid.getColumnConstraints().clear();
        sudokuGrid.getRowConstraints().clear();

        // Configure spacing and padding for the grid container
        sudokuGrid.setHgap(0);
        sudokuGrid.setVgap(0);
        sudokuGrid.setPadding(Insets.EMPTY);

        // Maintain fixed board size of 420x420 pixels
        sudokuGrid.setMinSize(420, 420);
        sudokuGrid.setPrefSize(420, 420);
        sudokuGrid.setMaxSize(420, 420);

        // Center the grid within the scene layout
        sudokuGrid.setAlignment(Pos.CENTER);

        // Dynamically build the 36 cells
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {

                TextField cell = new TextField();
                configureCell(cell); // Input filter restricting numbers from 1 to 6

                // Save the cell reference in the internal grid matrix
                cells[row][column] = cell;

                // Fixed size for each cell (70x70 px)
                cell.setPrefSize(70, 70);
                cell.setMinSize(70, 70);
                cell.setMaxSize(70, 70);

                // Center-align cell text
                cell.setAlignment(Pos.CENTER);

                // Calculate border thickness to outline 2x3 sub-blocks (UI/UX layout)
                int top = (row == 0) ? 2 : 0;
                int right = (column == 5 || column == 2) ? 2 : 1;
                int bottom = (row == 5 || row == 1 || row == 3) ? 2 : 1;
                int left = (column == 0) ? 2 : 0;

                // Define the base CSS styling for the cell
                String baseStyle =
                        "-fx-background-color: white;" +
                                "-fx-background-insets: 0;" +
                                "-fx-background-radius: 0;" +
                                "-fx-border-color: #52677F;" +
                                "-fx-border-width: " + top + " " + right + " " + bottom + " " + left + ";" +
                                "-fx-border-insets: 0;" +
                                "-fx-border-radius: 0;" +
                                "-fx-font-size: 20px;" +
                                "-fx-font-weight: bold;";

                // Apply default base style
                cell.setStyle(baseStyle);

                // Capture current coordinates for the lambda expression scope
                final int r = row;
                final int c = column;

                // Real-time listener to validate cell inputs dynamically (HU-4 requirement)
                cell.textProperty().addListener((observable, oldValue, newValue) -> {

                    // If the cell is empty, update the model, clear error message, and restore default style
                    if (newValue.isEmpty()) {
                        game.updateCell(r, c, 0);
                        cell.setStyle(baseStyle);
                        if (errorMessageLabel != null) {
                            errorMessageLabel.setText("");
                        }
                        return;
                    }

                    int value = Integer.parseInt(newValue);

                    // Update the model with the new value
                    game.updateCell(r, c, value);

                    // Validate the move using the model
                    if (!game.isValidMove(r, c, value)) {
                        cell.setStyle(baseStyle + "-fx-border-color: red;");
                        if (errorMessageLabel != null) {
                            errorMessageLabel.setText("Invalid move! Try again with another number.");
                        }
                    } else {
                        cell.setStyle(baseStyle);
                        if (errorMessageLabel != null) {
                            errorMessageLabel.setText("");
                        }
                    }
                });

                // Add cell component to the GridPane layout
                sudokuGrid.add(cell, column, row);
            }
        }

        // Generate the initial board state
        generateInitialBoard();
    }

    /**
     * Generates a new solved board state and places initial clues.
     */
    public void generateInitialBoard() {
        game.startNewGame();

        int[][] initialBoard = game.getBoard();

        // Clear error display if label exists
        if (errorMessageLabel != null) {
            errorMessageLabel.setText("");
        }

        // First: clear all cells
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {
                cells[row][column].clear();
                cells[row][column].setEditable(true);
            }
        }

        // Second: place initial clues
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {
                int number = initialBoard[row][column];

                if (number != 0) {
                    cells[row][column].setText(String.valueOf(number));
                    cells[row][column].setEditable(false);
                }
            }
        }
    }

    /**
     * Restricts inputs in cells to single digits from 1 to 6.
     */
    private void configureCell(TextField cell) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            if (newText.matches("[1-6]")) {
                return change;
            }

            logEvent("Invalid entry attempt: " + newText);
            return null;
        });

        cell.setTextFormatter(formatter);
    }

    // Action handler for "New Game" button in FXML
    @FXML
    private void onNewGameClick() {
        // 1. Create and configure confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm New Game");
        alert.setHeaderText("Start a new game?");
        alert.setContentText("If you reset the board, your progress will be lost.");

        // 2. Wait for user input
        Optional<ButtonType> result = alert.showAndWait();

        // 3. If user confirms, generate a new board and clear error messages
        if (result.isPresent() && result.get() == ButtonType.OK) {
            generateInitialBoard();
        }
    }

    // Action handler for "Help" button in FXML (HU-5 requirement)
    @FXML
    public void onHelpClick() {
        if (game.getAvailableHints() <= 0) {
            alertBox.showInformationBox(
                    "Help",
                    "No Hints Left",
                    "You have used all your available hints."
            );
            return;
        }

        int[] hint = game.provideHint();

        if (hint == null) {
            alertBox.showInformationBox(
                    "Help",
                    "Board Full",
                    "There are no empty cells left."
            );
            return;
        }

        int row = hint[0];
        int column = hint[1];
        int number = hint[2];

        cells[row][column].setText(String.valueOf(number));

        cells[row][column].setStyle(
                "-fx-background-color: #FFF3B0;" +
                        "-fx-border-color: #52677F;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 0;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;"
        );

        alertBox.showInformationBox(
                "Help",
                "Hint Provided",
                "A number has been revealed. Remaining hints: "
                        + game.getAvailableHints()
        );
    }

    // Action handler for "Exit" button in FXML returning to welcome view
    @FXML
    private void onExitClick(ActionEvent event) {
        Stage gameStage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        gameStage.hide();
        try {
            WelcomeView.getInstance().show();
        } catch (IOException e) {
            logEvent("Error returning to welcome view: " + e.getMessage());
        }
    }
}