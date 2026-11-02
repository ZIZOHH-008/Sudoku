package org.example.sudoku.models;

public class SudokuGame {

    private int[][] board;
    private int[][] solution;
    private int availableHints;

    private final SudokuGenerator generator = new SudokuGenerator();
    private final SudokuValidator validator = new SudokuValidator();
    private final SudokuHelper helper = new SudokuHelper();



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

    public int[][] getBoard() {
        int[][] boardCopy = new int[board.length][];

        for (int i = 0; i < board.length; i++) {
            boardCopy[i] = board[i].clone();
        }

        return boardCopy;
    }

    public int getAvailableHints() {
        return availableHints;
    }

    public void updateCell(int row, int column, int number) {
        board[row][column] = number;
    }

    /**
     * Checks whether a move follows Sudoku rules.
     */
    public boolean isValidMove(int row, int column, int number) {
        return validator.isValidMove(board, row, column, number);
    }


    /**
     * Provides a hint by filling the first empty cell.
     *
     * @return an array containing {row, column, number},
     *         or null if no hints are available or the board is full
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
}