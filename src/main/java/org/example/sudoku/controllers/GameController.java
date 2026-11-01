package org.example.sudoku.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.sudoku.models.IGameLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Main Controller for the 6x6 Sudoku Game.
 * Manages grid creation, button interactions, real-time validation (HU-4), and hint logic (HU-5).
 */
public class GameController implements IGameLog {

    @FXML
    private GridPane sudokuGrid;

    private final TextField[][] cells = new TextField[6][6];
    private final Random random = new Random();

    // Global attributes for solution tracking and hint counter (HU-5)
    private int[][] solution = new int[6][6];
    private int availableHints = 3;

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
                    // Clear error border if the cell is emptied
                    if (newValue.isEmpty()) {
                        cell.setStyle(baseStyle);
                        return;
                    }

                    // Reject input if it is not a digit from 1 to 6
                    if (!newValue.matches("[1-6]")) {
                        cell.setText(oldValue);
                        return;
                    }

                    // Check Sudoku rules in real-time (row, column, and 2x3 block)
                    if (!isValidMove(r, c, newValue)) {
                        // Highlight invalid entry with a red border while preserving base thickness
                        cell.setStyle(baseStyle + "-fx-border-color: red;");
                    } else {
                        // Restore default base styling for valid entry
                        cell.setStyle(baseStyle);
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
     * Validates whether placing a value at (row, col) respects Sudoku rules in real-time.
     */
    private boolean isValidMove(int row, int col, String value) {
        // 1. Validate Row
        for (int j = 0; j < 6; j++) {
            if (j != col && cells[row][j].getText().equals(value)) {
                return false;
            }
        }

        // 2. Validate Column
        for (int i = 0; i < 6; i++) {
            if (i != row && cells[i][col].getText().equals(value)) {
                return false;
            }
        }

        // 3. Validate 2x3 Sub-block
        int startRow = (row / 2) * 2;
        int startColumn = (col / 3) * 3;

        for (int i = startRow; i < startRow + 2; i++) {
            for (int j = startColumn; j < startColumn + 3; j++) {
                if ((i != row || j != col) && cells[i][j].getText().equals(value)) {
                    return false;
                }
            }
        }

        return true; // Valid entry
    }

    /**
     * Internal backtracking validation for the board solver algorithm.
     */
    private boolean isValid(int[][] board, int row, int column, int number) {
        // Verify row duplicates
        for (int c = 0; c < 6; c++) {
            if (board[row][c] == number) {
                return false;
            }
        }

        // Verify column duplicates
        for (int r = 0; r < 6; r++) {
            if (board[r][column] == number) {
                return false;
            }
        }

        // Determine 2x3 sub-block bounds
        int startRow = (row / 2) * 2;
        int startColumn = (column / 3) * 3;

        // Verify 2x3 sub-block duplicates
        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startColumn; c < startColumn + 3; c++) {
                if (board[r][c] == number) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Recursive solver using backtracking to build a complete Sudoku solution.
     */
    private boolean solveSudoku(int[][] board, int position) {
        if (position == 36) {
            return true;
        }

        int row = position / 6;
        int column = position % 6;

        List<Integer> numbers = new ArrayList<>();
        for (int number = 1; number <= 6; number++) {
            numbers.add(number);
        }
        Collections.shuffle(numbers, random);

        for (int number : numbers) {
            if (isValid(board, row, column, number)) {
                board[row][column] = number;

                if (solveSudoku(board, position + 1)) {
                    return true;
                }

                board[row][column] = 0;
            }
        }

        return false;
    }

    /**
     * Generates a new solved board state and places initial clues.
     */
    private void generateInitialBoard() {
        // Reset solution matrix and available hints counter
        solution = new int[6][6];
        availableHints = 3;

        if (!solveSudoku(solution, 0)) {
            throw new IllegalStateException("Unable to generate a valid Sudoku.");
        }

        // Clear all cell texts
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {
                cells[row][column].clear();
            }
        }

        // Place 2 initial clue numbers per 2x3 block
        for (int blockRow = 0; blockRow < 6; blockRow += 2) {
            for (int blockColumn = 0; blockColumn < 6; blockColumn += 3) {
                List<Integer> positions = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    positions.add(i);
                }

                Collections.shuffle(positions, random);

                for (int i = 0; i < 2; i++) {
                    int position = positions.get(i);
                    int row = blockRow + position / 3;
                    int column = blockColumn + position % 3;

                    cells[row][column].setText(String.valueOf(solution[row][column]));
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

            logEvent("Invalid entry");
            return null;
        });

        cell.setTextFormatter(formatter);
    }

    // Action handler for "New Game" button in FXML
    @FXML
    private void onNewGameClick(ActionEvent event) {
        generateInitialBoard();
        showNotification("New Game", "Sudoku Reset", "A new game board has been generated.");
    }

    // Action handler for "Help" button in FXML (HU-5 requirement)
    @FXML
    private void onHelpClick(ActionEvent event) {
        // 1. Check if the user has available hints left
        if (availableHints <= 0) {
            showNotification("Help", "No Hints Left", "You have used all your available hints for this game.");
            return;
        }

        // 2. Search for the first empty cell in the grid
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {
                TextField cell = cells[row][column];

                if (cell.getText().isEmpty()) {
                    // 3. Insert the exact correct value from the solution matrix
                    cell.setText(String.valueOf(solution[row][column]));

                    // 4. Calculate border thickness to preserve original 2x3 block grid layout
                    int top = (row == 0) ? 2 : 0;
                    int right = (column == 5 || column == 2) ? 2 : 1;
                    int bottom = (row == 5 || row == 1 || row == 3) ? 2 : 1;
                    int left = (column == 0) ? 2 : 0;

                    // 5. Visually highlight the suggested cell with a soft yellow background
                    String highlightStyle =
                            "-fx-background-color: #FFF9C4;" +
                                    "-fx-background-insets: 0;" +
                                    "-fx-background-radius: 0;" +
                                    "-fx-border-color: #1976D2;" +
                                    "-fx-border-width: " + top + " " + right + " " + bottom + " " + left + ";" +
                                    "-fx-border-insets: 0;" +
                                    "-fx-border-radius: 0;" +
                                    "-fx-font-size: 20px;" +
                                    "-fx-font-weight: bold;";

                    cell.setStyle(highlightStyle);

                    // 6. Decrement remaining hints
                    availableHints--;

                    showNotification("Help", "Hint Provided", "A valid number from the solution was suggested. Remaining hints: " + availableHints);

                    // Stop execution to provide only ONE hint per button click
                    return;
                }
            }
        }

        // Notify if the board has no empty cells
        showNotification("Help", "Board Full", "There are no empty cells left to give a hint.");
    }

    // Action handler for "Exit" button in FXML returning to welcome view
    @FXML
    private void onExitClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sudoku/welcome-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logEvent("Error returning to welcome view: " + e.getMessage());
        }
    }

    // Display dialog notification windows
    private void showNotification(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void logEvent(String eventMessage) {
        System.out.println("[LOG]: " + eventMessage);
    }
}