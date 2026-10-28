package org.example.sudoku.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;




public class GameController {
    @FXML private GridPane sudokuGrid;
    private final TextField[][] cells = new TextField[6][6];
    private final Random random = new Random();



    @FXML
    public void initialize() {
        // Remove constraints that may cause the cells to spread out
        sudokuGrid.getColumnConstraints().clear();
        sudokuGrid.getRowConstraints().clear();

        // Remove spacing between cells
        sudokuGrid.setHgap(0);
        sudokuGrid.setVgap(0);
        sudokuGrid.setPadding(Insets.EMPTY);

        // Keep the board at 420 x 420 pixels
        sudokuGrid.setMinSize(420, 420);
        sudokuGrid.setPrefSize(420, 420);
        sudokuGrid.setMaxSize(420, 420);

        // Center the cells within the board
        sudokuGrid.setAlignment(Pos.CENTER);

        // Create the 36 cells
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {

                TextField cell = new TextField();

                // Store the cell in the matrix
                cells[row][column] = cell;

                // Set a fixed size for all cells
                cell.setPrefSize(70, 70);
                cell.setMinSize(70, 70);
                cell.setMaxSize(70, 70);

                // Center the content
                cell.setAlignment(Pos.CENTER);

                // Calculate the border thickness
                int top = (row == 0) ? 2 : 0;
                int right = (column == 5) ? 2 :
                        (column == 2) ? 2 : 1;
                int bottom = (row == 5) ? 2 :
                        (row == 1 || row == 3) ? 2 : 1;
                int left = (column == 0) ? 2 : 0;

                // Apply styling to the cell
                cell.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-insets: 0;" +
                                "-fx-background-radius: 0;" +
                                "-fx-border-color: #52677F;" +
                                "-fx-border-width: " + top + " " +
                                right + " " + bottom + " " + left + ";" +
                                "-fx-border-insets: 0;" +
                                "-fx-border-radius: 0;" +
                                "-fx-font-size: 18px;"
                );

                // Add the cell to the GridPane
                sudokuGrid.add(cell, column, row);
            }
        }

        generateInitialBoard();
    }


    private boolean isValid(int[][] board, int row, int column, int number) {

        // Check that the number does not repeat in the row
        for (int c = 0; c < 6; c++) {
            if (board[row][c] == number) {
                return false;
            }
        }

        // Check that the number does not repeat in the column
        for (int r = 0; r < 6; r++) {
            if (board[r][column] == number) {
                return false;
            }
        }

        // Find the starting position of the 2-row by 3-column block
        int startRow = (row / 2) * 2;
        int startColumn = (column / 3) * 3;

        // Check that the number does not repeat in that 2x3 block
        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startColumn; c < startColumn + 3; c++) {
                if (board[r][c] == number) {
                    return false;
                }
            }
        }

        // If no repetition is found, the number is valid
        return true;
    }


    private boolean solveSudoku(int[][] board, int position) {

        // If all 36 cells have been processed, the Sudoku is complete
        if (position == 36) {
            return true;
        }

        // Convert the linear position into a row and column
        int row = position / 6;
        int column = position % 6;

        // Prepare the numbers from 1 to 6 in random order
        List<Integer> numbers = new ArrayList<>();


        for (int number = 1; number <= 6; number++) {
            numbers.add(number);
        }

        Collections.shuffle(numbers, random);


        // Try placing each number
        for (int number : numbers) {

            if (isValid(board, row, column, number)) {
                board[row][column] = number;

                // Continue with the next cell
                if (solveSudoku(board, position + 1)) {
                    return true;
                }

                // If the puzzle cannot be completed, undo this choice
                board[row][column] = 0;
            }
        }

        // No number worked at this position
        return false;
    }


    private void generateInitialBoard() {

        // Create a matrix to build the complete solution
        int[][] solution = new int[6][6];

        // Generate a solution that follows all the rules
        if (!solveSudoku(solution, 0)) {
            throw new IllegalStateException(
                    "Unable to generate a valid Sudoku."
            );
        }

        // Clear the 36 cells in the interface
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {
                cells[row][column].clear();
            }
        }

        // Iterate through the six blocks of 2 rows by 3 columns
        for (int blockRow = 0; blockRow < 6; blockRow += 2) {
            for (int blockColumn = 0; blockColumn < 6; blockColumn += 3) {

                // There are six possible positions in each block
                List<Integer> positions = new ArrayList<>();

                for (int i = 0; i < 6; i++) {
                    positions.add(i);
                }

                // Shuffle the positions to select them randomly
                Collections.shuffle(positions, random);

                // Display two numbers from the solution in this block
                for (int i = 0; i < 2; i++) {

                    int position = positions.get(i);

                    int row = blockRow + position / 3;
                    int column = blockColumn + position % 3;

                    cells[row][column].setText(
                            String.valueOf(solution[row][column])
                    );
                }
            }
        }
    }
}