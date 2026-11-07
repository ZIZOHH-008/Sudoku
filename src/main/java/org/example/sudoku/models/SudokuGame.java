package org.example.sudoku.models;
/**
 * Represents a Sudoku game and manages its board, solution, and hints.
 */
public class SudokuGame {

    /** Stores the current Sudoku board. */
    private int[][] board;

    /** Stores the complete Sudoku solution. */
    private int[][] solution;

    /** Stores the number of available hints. */
    private int availableHints;

    /** Generates Sudoku boards and solutions. */
    private final SudokuGenerator generator = new SudokuGenerator();

    /** Validates Sudoku moves. */
    private final SudokuValidator validator = new SudokuValidator();

    /** Provides hints for the Sudoku board. */
    private final SudokuHelper helper = new SudokuHelper();


    /**
     * Creates a new Sudoku game with three available hints.
     */
    public SudokuGame() {
        this.availableHints = 3;
    }


    /**
     * Starts a new Sudoku game.
     */
    public void startNewGame() {
        solution = generator.generateSolution();
        board = generator.generateInitialBoard(solution);
        availableHints = 3;
    }

    /**
     * Returns a copy of the current board.
     *
     * @return a copy of the Sudoku board
     */
    public int[][] getBoard() {
        int[][] boardCopy = new int[board.length][];

        for (int i = 0; i < board.length; i++) {
            boardCopy[i] = board[i].clone();
        }

        return boardCopy;
    }

    /**
     * Returns the number of available hints.
     *
     * @return the remaining hints
     */
    public int getAvailableHints() {
        return availableHints;
    }

    /**
     * Updates the value of a board cell.
     *
     * @param row the cell's row
     * @param column the cell's column
     * @param number the value to set
     */
    public void updateCell(int row, int column, int number) {
        board[row][column] = number;
    }


    /**
     * Checks whether a move follows Sudoku rules.
     *
     * @param row the cell's row
     * @param column the cell's column
     * @param number the number to validate
     * @return {@code true} if the move is valid; {@code false} otherwise
     */
    public boolean isValidMove(int row, int column, int number) {
        return validator.isValidMove(board, row, column, number);
    }


    /**
     * Provides a hint by filling the first empty cell.
     *
     * @return an array containing {row, column, number},
     *         or {@code null} if no hints are available or the board is full
     */
    public int[] provideHint() {
        if (availableHints <= 0) {
            return null;
        }

        int[] hint = helper.getHint(board, solution);

        if (hint != null) {
            board[hint[0]][hint[1]] = hint[2];
            availableHints--;
        }

        return hint;
    }



    /**
     * Checks whether the board is completely filled and matches the solution.
     *
     * @return {@code true} if the board is solved; {@code false} otherwise
     */
    public boolean isSolved() {
        if (board == null) {
            return false;
        }

        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {

                int number = board[row][column];

                // No puede haber casillas vacías ni números fuera del rango.
                if (number < 1 || number > 6) {
                    return false;
                }

                // Comprobar fila y columna.
                for (int i = 0; i < 6; i++) {

                    if (i != column && board[row][i] == number) {
                        return false;
                    }

                    if (i != row && board[i][column] == number) {
                        return false;
                    }
                }

                // Comprobar bloque de 2x3.
                int startRow = (row / 2) * 2;
                int startColumn = (column / 3) * 3;

                for (int r = startRow; r < startRow + 2; r++) {
                    for (int c = startColumn; c < startColumn + 3; c++) {

                        if ((r != row || c != column)
                                && board[r][c] == number) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}