
package org.example.sudoku.models;

/**
 * Validates user moves according to the rules of a 6x6 Sudoku game.
 * <p>
 * Checks that a number does not appear more than once in the same row, column, or 2x3 block.
 * </p>
 */
public class SudokuValidator {

    /**
     * Defines the total number of rows and columns in the Sudoku board.
     */
    private static final int SIZE = 6;

    /**
     * Defines the number of rows in each Sudoku block.
     */
    private static final int BLOCK_ROWS = 2;

    /**
     * Defines the number of columns in each Sudoku block.
     */
    private static final int BLOCK_COLUMNS = 3;


    /**
     * Checks whether a number can be placed in a specific position without violating Sudoku rules.
     * <p>
     * Validates the board dimensions, the position coordinates, the
     * allowed number range, and checks for duplicates in the corresponding row, column, and 2x3 block.
     *
     * @param board the Sudoku board represented as a two-dimensional array
     * @param row the row index where the number is intended to be placed
     * @param column the column index where the number is intended to be placed
     * @param number the number to validate, ranging from 1 to 6
     * @return {@code true} if the move does not violate Sudoku rules;
     *         {@code false} otherwise
     */
    public boolean isValidMove(int[][] board, int row, int column, int number) {

        if (board == null || board.length != SIZE
                || row < 0 || row >= SIZE
                || column < 0 || column >= SIZE
                || number < 1 || number > SIZE) {
            return false;
        }

        // Check row
        for (int c = 0; c < SIZE; c++) {
            if (c != column && board[row][c] == number) {
                return false;
            }
        }

        // Check column
        for (int r = 0; r < SIZE; r++) {
            if (r != row && board[r][column] == number) {
                return false;
            }
        }

        // Check 2x3 block
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startColumn = (column / BLOCK_COLUMNS) * BLOCK_COLUMNS;

        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startColumn;
                 c < startColumn + BLOCK_COLUMNS;
                 c++) {

                if ((r != row || c != column)
                        && board[r][c] == number) {
                    return false;
                }
            }
        }

        return true;
    }
}