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
 * Controlador principal del juego Sudoku (6x6).
 * Administra la creación de la cuadrícula, interacción de botones y lógica de tablero.
 */
public class GameController implements IGameLog {

    @FXML
    private GridPane sudokuGrid;

    private final TextField[][] cells = new TextField[6][6];
    private final Random random = new Random();

    @FXML
    public void initialize() {
        // Eliminar restricciones que causen distorsión en la cuadrícula
        sudokuGrid.getColumnConstraints().clear();
        sudokuGrid.getRowConstraints().clear();

        // Configuración de espaciado y relleno del contenedor
        sudokuGrid.setHgap(0);
        sudokuGrid.setVgap(0);
        sudokuGrid.setPadding(Insets.EMPTY);

        // Mantener el tamaño exacto del tablero en 420 x 420 píxeles
        sudokuGrid.setMinSize(420, 420);
        sudokuGrid.setPrefSize(420, 420);
        sudokuGrid.setMaxSize(420, 420);

        // Centrar la cuadrícula dentro de la escena
        sudokuGrid.setAlignment(Pos.CENTER);

        // Crear dinámicamente las 36 celdas
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {

                TextField cell = new TextField();
                configureCell(cell); // Filtro para restringir entradas de 1 a 6

                // Registrar evento al presionar Enter en una celda
                cell.setOnAction(event -> {
                    String number = cell.getText();
                    logEvent("Entered number: " + number);
                });

                // Guardar la celda en la matriz interna
                cells[row][column] = cell;

                // Tamaño fijo para todas las celdas (70x70 px)
                cell.setPrefSize(70, 70);
                cell.setMinSize(70, 70);
                cell.setMaxSize(70, 70);

                // Centrar el contenido de la celda
                cell.setAlignment(Pos.CENTER);

                // Cálculo exacto de grosores de borde para separar bloques 2x3 (UI/UX)
                int top = (row == 0) ? 2 : 0;
                int right = (column == 5 || column == 2) ? 2 : 1;
                int bottom = (row == 5 || row == 1 || row == 3) ? 2 : 1;
                int left = (column == 0) ? 2 : 0;

                // Aplicar estilo limpio de bordes directamente en la celda
                cell.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-insets: 0;" +
                                "-fx-background-radius: 0;" +
                                "-fx-border-color: #52677F;" +
                                "-fx-border-width: " + top + " " + right + " " + bottom + " " + left + ";" +
                                "-fx-border-insets: 0;" +
                                "-fx-border-radius: 0;" +
                                "-fx-font-size: 20px;" +
                                "-fx-font-weight: bold;"
                );

                // Agregar la celda al GridPane
                sudokuGrid.add(cell, column, row);
            }
        }

        // Generar los números iniciales del tablero de Sudoku
        generateInitialBoard();
    }

    private boolean isValid(int[][] board, int row, int column, int number) {
        // Verificar repetición en la fila
        for (int c = 0; c < 6; c++) {
            if (board[row][c] == number) {
                return false;
            }
        }

        // Verificar repetición en la columna
        for (int r = 0; r < 6; r++) {
            if (board[r][column] == number) {
                return false;
            }
        }

        // Determinar inicio del bloque de 2x3
        int startRow = (row / 2) * 2;
        int startColumn = (column / 3) * 3;

        // Verificar repetición dentro del bloque de 2x3
        for (int r = startRow; r < startRow + 2; r++) {
            for (int c = startColumn; c < startColumn + 3; c++) {
                if (board[r][c] == number) {
                    return false;
                }
            }
        }

        return true;
    }

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

    private void generateInitialBoard() {
        int[][] solution = new int[6][6];

        if (!solveSudoku(solution, 0)) {
            throw new IllegalStateException("Unable to generate a valid Sudoku.");
        }

        // Limpiar todas las celdas
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {
                cells[row][column].clear();
            }
        }

        // Colocar 2 números de ayuda por cada bloque de 2x3
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

    // Acción para el botón "New Game" en FXML
    @FXML
    private void onNewGameClick(ActionEvent event) {
        generateInitialBoard();
        showNotification("New Game", "Sudoku Reset", "A new game board has been generated.");
    }

    // Acción para el botón "Help" en FXML
    @FXML
    private void onHelpClick(ActionEvent event) {
        showNotification("Help", "Hint Request", "Help feature will suggest a valid number for an empty cell.");
    }

    // Acción para el botón "Exit" en FXML para regresar a la vista de bienvenida
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

    // Mostrar notificaciones de diálogo
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