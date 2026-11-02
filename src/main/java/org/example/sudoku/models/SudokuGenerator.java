
package org.example.sudoku.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    private static final int SIZE = 6;
    private static final int BLOCK_ROWS = 2;
    private static final int BLOCK_COLUMNS = 3;

    private final Random random = new Random();


    /**
     * Generates a complete and valid Sudoku solution.
     */
    public int[][] generateSolution() {
        int[][] solution = new int[SIZE][SIZE];

        if (!solveSudoku(solution, 0)) {
            throw new IllegalStateException(
                    "Unable to generate a valid Sudoku."
            );
        }

        return solution;
    }

    /**
     * Generates an initial board with two clues per 2x3 block.
     * Empty positions are represented by zero.
     */
    public int[][] generateInitialBoard(int[][] solution) {

        if (solution == null || solution.length != SIZE) {
            throw new IllegalArgumentException(
                    "Invalid Sudoku solution."
            );
        }

        int[][] board = new int[SIZE][SIZE];

        for (int blockRow = 0; blockRow < SIZE; blockRow += BLOCK_ROWS) {
            for (int blockColumn = 0;
                 blockColumn < SIZE;
                 blockColumn += BLOCK_COLUMNS) {

                List<Integer> positions = new ArrayList<>();

                for (int i = 0; i < BLOCK_ROWS * BLOCK_COLUMNS; i++) {
                    positions.add(i);
                }

                Collections.shuffle(positions, random);

                for (int i = 0; i < 2; i++) {
                    int position = positions.get(i);

                    int row = blockRow + position / BLOCK_COLUMNS;
                    int column = blockColumn + position % BLOCK_COLUMNS;

                    board[row][column] = solution[row][column];
                }
            }
        }

        return board;
    }

    /**
     * Recursive backtracking algorithm.
     */
    private boolean solveSudoku(int[][] board, int position) {

        if (position == SIZE * SIZE) {
            return true;
        }

        int row = position / SIZE;
        int column = position % SIZE;

        List<Integer> numbers = new ArrayList<>();

        for (int number = 1; number <= SIZE; number++) {
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
     * Checks whether a number can be placed in a position.
     */
    private boolean isValid(
            int[][] board,
            int row,
            int column,
            int number
    ) {

        // Check row
        for (int c = 0; c < SIZE; c++) {
            if (board[row][c] == number) {
                return false;
            }
        }

        // Check column
        for (int r = 0; r < SIZE; r++) {
            if (board[r][column] == number) {
                return false;
            }
        }

        // Check 2x3
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startColumn = (column / BLOCK_COLUMNS) * BLOCK_COLUMNS;

        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startColumn;
                 c < startColumn + BLOCK_COLUMNS;
                 c++) {

                if (board[r][c] == number) {
                    return false;
                }
            }
        }

        return true;
    }
}
