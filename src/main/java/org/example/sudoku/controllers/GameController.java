package org.example.sudoku.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import org.example.sudoku.models.IGameLog;
import org.example.sudoku.models.SudokuGenerator;
import java.util.List;




/* NOTAS TEMPORALES PARA ENTENDER LA LÓGICA DEL CÓDIGO
- cell.getText()  - es lo que tiene la celda
- getControlNewText()  -  el valor que va a tener la celda (si es válido)

This controller:
- Handles cell events, such as when the user presses Enter.
- Applies visual styles to the cells.
- Determines which cells are editable and which contain initial numbers.
*/



public class GameController implements IGameLog {
    @FXML private GridPane sudokuGrid;
    private final TextField[][] cells = new TextField[6][6];
    private final SudokuGenerator sudokuGenerator = new SudokuGenerator();


    //sets up the 6×6 grid and creates its 36 cells
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
                configureCell(cell); //method below of this one

                //The alert only appears when the user presses Enter after typing
                cell.setOnAction(event -> {
                    String number = cell.getText();
                    logEvent("Entered number: " + number);
                });

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


    //requests the initial matrix from the model and displays its numbers in the interface
    private void generateInitialBoard() {
        // Obtener la matriz inicial desde el modelo
        int[][] initialBoard = sudokuGenerator.generateInitialBoard();

        // Recorrer las 36 casillas de la interfaz
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 6; column++) {

                cells[row][column].clear();

                if (initialBoard[row][column] != 0) {
                    cells[row][column].setText(
                            String.valueOf(initialBoard[row][column])
                    );
                    cells[row][column].setEditable(false);
                } else {
                    cells[row][column].setEditable(true);
                }
            }
        }
    }


    //configures the input filter to allow only numbers from 1 to 6.
    private void configureCell(TextField cell) {
    //TextFormatter permite establecer un filtro sobre el contenido de un TextField

        /* === Nota para entender el TextFormatter ===
        TextFormatter de tipo <String>; su nombre es formatter;
        change representa el cambio que el usuario intenta realizar en el TextField.
        La expresión change -> es una expresión lambda.
        Permite definir qué hacer con cada modificación que se intenta realizar
        */

        TextFormatter<String> formatter = new TextFormatter<>(change -> {

            String newText = change.getControlNewText(); //Obtains the content that the TextField would have if the change is accepted

            // Allow an empty cell, without this, we cannot delete entries
            if (newText.isEmpty()) { //borrar contenido de la celda
                return change;
            }

            // Allow only numbers from 1 to 6
            if (newText.matches("[1-6]")) { //Comprueba si el es un digito entre 1 y 6.
                return change; //Aquí repetía muchas veces las entradas
            }

            // Reject any other input
            logEvent("Invalid entry");
            return null;
        });

        cell.setTextFormatter(formatter);
        //Assign a textFormatter to the parameter cell
    }

}