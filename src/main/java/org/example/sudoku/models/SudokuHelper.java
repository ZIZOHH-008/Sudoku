
package org.example.sudoku.models;

public class SudokuHelper {

    private static final int SIZE = 6;

    /**
     * Finds the first empty cell in the board and returns
     * the corresponding value from the solution.
     *
     * @param board current Sudoku board
     * @param solution complete Sudoku solution
     * @return an array containing {row, column, number},
     *         or null if there are no empty cells
     */
    public int[] getHint(int[][] board, int[][] solution) {

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {

                if (board[row][column] == 0) {
                    return new int[]{
                            row,
                            column,
                            solution[row][column]
                    };
                }
            }
        }

        return null;
    }
}
