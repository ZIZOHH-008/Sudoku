package org.example.sudoku.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;





public class GameController {
    @FXML
    private GridPane gridSudoku;

    private final TextField[][] celdas = new TextField[6][6];




    @FXML
    public void initialize() {
        // Eliminar restricciones que puedan repartir las celdas
        gridSudoku.getColumnConstraints().clear();
        gridSudoku.getRowConstraints().clear();

        // Eliminar espacios entre celdas
        gridSudoku.setHgap(0);
        gridSudoku.setVgap(0);
        gridSudoku.setPadding(Insets.EMPTY);

        // Mantener el tablero en 300 x 300 píxeles
        gridSudoku.setMinSize(420, 420);
        gridSudoku.setPrefSize(420, 420);
        gridSudoku.setMaxSize(420, 420);

        // Centrar las celdas dentro del tablero
        gridSudoku.setAlignment(Pos.CENTER);


        // Crear las 36 celdas
        for (int fila = 0; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {

                TextField celda = new TextField();

                // Guardar la celda en la matriz
                celdas[fila][columna] = celda;

                // Tamaño fijo para todas las celdas
                celda.setPrefSize(70, 70);
                celda.setMinSize(70, 70);
                celda.setMaxSize(70, 70);

                // Centrar el contenido
                celda.setAlignment(Pos.CENTER);

                // Calcular el grosor de los bordes
                int arriba = (fila == 0) ? 2 : 0;
                int derecha = (columna == 5) ? 2 :
                        (columna == 2) ? 2 : 1;
                int abajo = (fila == 5) ? 2 :
                        (fila == 1 || fila == 3) ? 2 : 1;
                int izquierda = (columna == 0) ? 2 : 0;

                // Aplicar estilo a la celda
                celda.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-insets: 0;" +
                                "-fx-background-radius: 0;" +
                                "-fx-border-color: #52677F;" +
                                "-fx-border-width: " + arriba + " " +
                                derecha + " " + abajo + " " + izquierda + ";" +
                                "-fx-border-insets: 0;" +
                                "-fx-border-radius: 0;" +
                                "-fx-font-size: 18px;"
                );

                // Agregar la celda al GridPane
                gridSudoku.add(celda, columna, fila);
            }
        }
    }
}