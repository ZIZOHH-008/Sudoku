package org.example.sudoku.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;




public class SudokuGenerator {
    private final Random random = new Random();

    //constructs the initial matrix by selecting two numbers for each 2×3 block
    public int[][] generateInitialBoard() {
        int[][] solution = generateSolution();
        int[][] initialBoard = new int[6][6];

        // Recorrer los seis bloques de 2 filas por 3 columnas
        for (int blockRow = 0; blockRow < 6; blockRow += 2) {
            for (int blockColumn = 0; blockColumn < 6; blockColumn += 3) {

                // Obtener seis posiciones aleatorias
                List<Integer> positions = generateRandomPositions();

                // Seleccionar dos posiciones del bloque
                for (int i = 0; i < 2; i++) {
                    int position = positions.get(i);

                    int row = blockRow + position / 3;
                    int column = blockColumn + position % 3;

                    initialBoard[row][column] = solution[row][column];
                }
            }
        }

        return initialBoard;
    }

    //constructs a complete solution using a solving algorithm.
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

    //checks that a number does not repeat within the same row, column, or 2×3 block
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

    //generates a 6×6 matrix containing a complete solution
    public int[][] generateSolution() {
        int[][] board = new int[6][6];
        solveSudoku(board, 0);
        return board;
    }

    //generates six positions in a random order
    public List<Integer> generateRandomPositions() {
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            positions.add(i);
        }

        Collections.shuffle(positions, random);

        return positions;
    }

}
