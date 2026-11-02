
package org.example.sudoku.models;

public class SudokuValidator {

    private static final int SIZE = 6;
    private static final int BLOCK_ROWS = 2;
    private static final int BLOCK_COLUMNS = 3;

    /**
     * Checks whether a number can be placed in a position
     * without violating Sudoku rules.
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
